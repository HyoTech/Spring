package com.example.shop.User;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.RequiredArgsConstructor;

@Service
@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/UserInput")
    public String UsrInputForm(Authentication auth) {
        if (auth != null && auth.isAuthenticated()) {
            return "redirect:/list";
        }
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

    @GetMapping("/my-page")
    public String mypage(Authentication auth) {
        System.out.println(auth.isAuthenticated());
        System.out.println(auth.getName());
        System.out.println(auth.getAuthorities().contains(new SimpleGrantedAuthority("일반유저")));
        return "mypage.html";
    }

}
