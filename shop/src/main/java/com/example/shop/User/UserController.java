package com.example.shop.User;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.shop.User.MyUserDetailsService.CustomUser;

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
        return "redirect:/list/page/1";
    }

    @GetMapping("/login")
    public String login() {
        return "login.html";
    }

    @GetMapping("/my-page")
    public String mypage(Authentication auth) {
        CustomUser user = (CustomUser) auth.getPrincipal();
        System.out.println(user.displayName);
        return "mypage.html";
    }

    @GetMapping("/user/1")
    @ResponseBody
    public UserDto getuser() {
        return userService.DetailUser();
    }

}
