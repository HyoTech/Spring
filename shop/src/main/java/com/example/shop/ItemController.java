package com.example.shop;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.*;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ItemController {
    private final ItemRepository itemRepository;
    private final InfoRepository infoRepository;

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
}
