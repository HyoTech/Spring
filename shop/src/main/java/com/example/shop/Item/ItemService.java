package com.example.shop.Item;

import java.util.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.shop.Infomation.InfoRepository;
import com.example.shop.Infomation.Information;
import com.example.shop.User.UserInfo;
import com.example.shop.User.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final InfoRepository infoRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;

    // 상품 보여주는 기능
    public void showList(Model model) {
        // 최근 등록된 상품부터 보여주기
        List<Item> ItemResult = itemRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        // 1. 서버 API 함수의 파라미터에 Model model 넣고
        // 2. API안에서 model.addAttribute("작명", 전송할데이터)
        // 3. html 태그에 th:text="${작명}"
        model.addAttribute("items", ItemResult);
    }

    // 공지사항 보여주는 기능
    public void showInfoList(Model model) {
        List<Information> InfoResult = infoRepository.findAll();
        model.addAttribute("infos", InfoResult);
    }

    // 상품입력 기능
    public void addItem(String title, Integer price, String writer, String image, Authentication auth) {
        Optional<UserInfo> result = userRepository.findByUserName(auth.getName());
        if (result.get().getAuthLevel() == 1 || result.get().getAuthLevel() == 2) {
            Item newItem = new Item();
            newItem.setProductName(title);
            newItem.setPrice(price);
            newItem.setWriter(writer);
            newItem.setImgurl(image);
            itemRepository.save(newItem);
        }
    }

    // 상품 상세페이지 보여주기
    public void showDetail(@PathVariable("id") long id, Model model) {
        Optional<Item> result = itemRepository.findById(id);

        if (result.isPresent()) {
            model.addAttribute("ditem", result.get());
        }
    }

    // 상품수정하는 기능
    // 1, 리스트에서 수정 버튼 추가
    // 2, 수정 버튼 누를 시 수정 페이지로 이동
    // 3, 수정 버튼을 누른 상품의 아이디를 받아서 수정 페이지에서 정보 수정 시 맞는 행 찾아서 정보 업데이트
    public void modInfo(@PathVariable("id") long id, Model model) {
        Optional<Item> result = itemRepository.findById(id);

        if (result.isPresent()) {
            model.addAttribute("mitem", result.get());
        }
    }

    // 1, 기존에 사용하던 aws 사진 경로에 있던 사진을 삭제 후
    // 2, 저장을 하는게 이후 관리에 도움이될 것 같다
    // 3, deleteFileFromS3 -> createPresignedUrl
    @Transactional
    public void modItem(@PathVariable("id") long id, String title, Integer price, String image, String oldImageUrl) {
        String[] oldUrl = oldImageUrl.split("com/");
        String resultUrl = oldUrl.length > 1 ? oldUrl[1] : "";

        Item item = itemRepository.findById(id).orElseThrow(() -> {
            throw new IllegalArgumentException("해당하는 상품이 없습니다 id : " + id);
        });

        if (title.length() <= 100) {
            item.setProductName(title);
        } else {
            System.out.println("100자를 넘어갔습니다.");
        }

        if (price > 0) {
            item.setPrice(price);
        } else {
            System.out.println("유효하지 않은 가격입니다.");
        }

        if (!oldImageUrl.isEmpty()) {
            s3Service.updateFileIn3(resultUrl, image);
        }
        item.setImgurl(image);
    }

    // 상품삭제기능 ID를 통해 행삭제
    public boolean DeItem(@PathVariable("id") Long id, Authentication auth) {
        Optional<UserInfo> result = userRepository.findByUserName(auth.getName());
        Optional<Item> item = itemRepository.findById(id);

        if (item.isPresent()
                && (item.get().getWriter().equals(result.get().getUserName()) || result.get().getAuthLevel() == 1)) {
            itemRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public void paging(Model model, @PathVariable("id") Integer id) {
        Page<Item> result = itemRepository.findPageBy(PageRequest.of(id - 1, 5));
        model.addAttribute("items", result);
        model.addAttribute("currentPage", id);
    }

    public void searchItem(Model model, String searchText, int page) {
        Pageable pageable = PageRequest.of(page, 5);
        Page<Item> result = itemRepository.fullTextSearch(searchText, pageable);

        model.addAttribute("searchText", searchText);
        model.addAttribute("searchItem", result);
        model.addAttribute("currentPage", page);
    }
}