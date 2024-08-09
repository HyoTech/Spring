package com.example.shop.User;

import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/UserInput")
    public String UsrInputForm() {
        return "CreateUser.html";
    }

    @PostMapping("/CreateUser")
    public String CreateUser(String username, String password, String name) throws Exception {
        userService.CrtUser(username, password, name);
        return "redirect:/list";
    }

    @GetMapping("/login")
    public String login() {
        return "login.html";
    }

}
