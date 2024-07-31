package com.example.shop;

import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final InfoRepository infoRepository;

    // 상품 보여주는 기능
    public void showList(Model model) {
        List<Item> ItemResult = itemRepository.findAll();
        List<Information> InfoResult = infoRepository.findAll();
        // 1. 서버 API 함수의 파라미터에 Model model 넣고
        // 2. API안에서 model.addAttribute("작명", 전송할데이터)
        // 3. html 태그에 th:text="${작명}"
        model.addAttribute("items", ItemResult);
        model.addAttribute("infos", InfoResult);
    }

    // 상품입력 기능
    public void addItem(String title, Integer price) {
        Item newItem = new Item();
        newItem.setProductName(title);
        newItem.setPrice(price);
        itemRepository.save(newItem);
    }

    // 상품 상세페이지 보여주기
    public void showDetail(@PathVariable long id, Model model) {
        Optional<Item> result = itemRepository.findById(id);

        if (result.isPresent()) {
            model.addAttribute("ditem", result.get());
        }
    }
}