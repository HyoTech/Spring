package com.example.shop;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ItemController {
    @GetMapping("/list")
    String list() {
        return "list.html";
    }
}
