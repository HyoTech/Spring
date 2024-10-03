package com.example.shop;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.shop.Item.ItemService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class BasicController {
    private final ItemService itemService;

    @GetMapping("/")
    String index(Model model) {
        itemService.showList(model);
        itemService.showInfoList(model);
        return "index.html";
    }
}
