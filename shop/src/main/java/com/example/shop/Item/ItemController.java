package com.example.shop.Item;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.shop.Comment.Comment;
import com.example.shop.Comment.CommentRepository;

@Service
@Controller
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final ItemRepository itemRepository;
    private final S3Service s3Service;
    private final CommentRepository commentRepository;

    // 공지사항 API
    @GetMapping("/info")
    public String showInfo(Model model) {
        itemService.showInfoList(model);
        return "info.html";
    }

    // 상품입력 폼으로 이동
    @GetMapping("/write")
    String write() {
        return "write.html";
    }

    // 상품입력 폼에서 작성한 상품 정보들을 서버로 보내주어 검사 후 DB에 저장
    @PostMapping("/add")
    String postWrite(@RequestParam String title, @RequestParam Integer price, @RequestParam String writer,
            @RequestParam String image) {
        itemService.addItem(title, price, writer, image);
        return "redirect:/list/page/1";
    }

    // 상품상세페이지, 아이템 테이블의 ID컬럼을 이용하여 몇번째 상품인지 확인
    // URL ID를 이용해서 items의 id에 맞게 상세페이지를 보여주기
    @GetMapping("/list/page/detail/{id}")
    String detail(@PathVariable("id") long id, Model model) {
        List<Comment> commentResult = commentRepository.findByParentId(id);
        model.addAttribute("comment", commentResult);
        itemService.showDetail(id, model);
        return "detail.html";
    }

    // 상품 수정 관련 API
    @GetMapping("/list/page/modify/{id}")
    public String modify(@PathVariable("id") long id, Model model) {
        itemService.modInfo(id, model);
        return "modify.html";
    }

    @PostMapping("/mod/{id}")
    String postModify(@PathVariable("id") long id, @RequestParam("title") String title,
            @RequestParam("price") Integer price) {
        itemService.modItem(id, title, price);
        return "redirect:/list/page/1";
    }

    // 상품 삭제 API
    @DeleteMapping("/del/{id}")
    ResponseEntity<String> deleItem(@PathVariable("id") Long id) {
        itemService.DeItem(id);
        return ResponseEntity.status(200).body("삭제완료");
    }

    // DB에 있는 상품명과 가격을 카드형태로 보여줌, 페이징 기능
    @GetMapping("/list/page/{id}")
    String pagelist(Model model, @PathVariable("id") Integer id) {
        Page<Item> result = itemRepository.findPageBy(PageRequest.of(id - 1, 5));
        System.out.println(result);
        model.addAttribute("items", result);
        model.addAttribute("currentPage", id);
        return "list.html";
    }

    // presigned-url 요청을 받으면 사용자 쪽으로 url을 전달해주는 API
    @GetMapping("presigned-url")
    @ResponseBody
    String getURL(@RequestParam String filename) {
        var result = s3Service.createPresignedUrl("test/" + filename);
        return result;
    }

    @GetMapping("/search")
    String getSearch(Model model, @RequestParam String searchText, @RequestParam(defaultValue = "0") int page) {
        Pageable Pageable = PageRequest.of(page, 5);
        Page<Item> result = itemRepository.fullTextSearch(searchText, Pageable);
        model.addAttribute("searchText", searchText);
        model.addAttribute("searchItem", result);
        model.addAttribute("currentPage", page);
        return "search.html";
    }
}