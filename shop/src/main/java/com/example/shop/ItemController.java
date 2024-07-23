package com.example.shop;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ItemController {

    // 1. 서버 API 함수의 파라미터에 Model model 넣고
    // 2. API안에서 model.addAttribute("작명", 전송할데이터)
    // 3. html 태그에 th:text="${작명}"
    @GetMapping("/list")
    String list(Model model) {
        model.addAttribute("name", "홍길동");
        return "list.html";
    }
}
