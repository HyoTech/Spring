package com.example.shop;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.*;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;

@Controller
@RequiredArgsConstructor
public class ItemController {
    private final ItemRepository itemRepository;
    private final InfoRepository infoRepository;

    // DB에 있는 상품명과 가격을 카드형태로 보여줌
    @GetMapping("/list")
    String list(Model model) {
        List<Item> ItemResult = itemRepository.findAll();
        List<Information> InfoResult = infoRepository.findAll();
        // 1. 서버 API 함수의 파라미터에 Model model 넣고
        // 2. API안에서 model.addAttribute("작명", 전송할데이터)
        // 3. html 태그에 th:text="${작명}"
        model.addAttribute("items", ItemResult);
        model.addAttribute("infos", InfoResult);
        return "list.html";
    }

    // 상품입력 폼으로 이동
    @GetMapping("/write")
    String write() {
        return "write.html";
    }

    // 상품입력 폼에서 작성한 상품 정보들을 서버로 보내주어 검사 후 DB에 저장
    @PostMapping("/add")
    String postWrite(@RequestParam String title, @RequestParam Integer price) {
        Item newItem = new Item();
        newItem.setProductName(title);
        newItem.setPrice(price);
        itemRepository.save(newItem);
        return "redirect:/list";
    }

    // 상품상세페이지, 아이템 테이블의 ID컬럼을 이용하여 몇번째 상품인지 확인
    // URL ID를 이용해서 items의 id에 맞게 상세페이지를 보여주기
    @GetMapping("/detail/{id}")
    String detail(@PathVariable long id, Model model) {
        Optional<Item> result = itemRepository.findById(id);

        if (result.isPresent()) {
            model.addAttribute("ditem", result.get());
            return "detail.html";
        } else {
            System.out.println("Item not found for id: " + id);
            return "list.html";
        }
    }
}
