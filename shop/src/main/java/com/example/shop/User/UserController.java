package com.example.shop.User;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.shop.Sales.OrdersService;

import org.springframework.ui.Model;
import lombok.RequiredArgsConstructor;

@Service
@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final OrdersService ordersService;

    @GetMapping("/UserInput")
    public String UsrInputForm(Authentication auth) {
        if (auth != null && auth.isAuthenticated()) {
            return "redirect:/list";
        }
        return "CreateUser.html";
    }

    @PostMapping("/CreateUser")
    public String CreateUser(@RequestParam("userName") String userName,
            @RequestParam("password") String password,
            @RequestParam("displayName") String displayName,
            @RequestParam("email") String email,
            @RequestParam("authLevel") Integer authLevel) throws Exception {
        userService.CrtUser(userName, password, displayName, email, authLevel);
        return "redirect:/list/page/1";
    }

    @GetMapping("/login")
    public String login() {
        return "login.html";
    }

    @GetMapping("/my-page")
    public String mypage(Authentication auth, Model model) {
        userService.mypages(auth, model);
        ordersService.myOrders(model, auth);
        return "mypage.html";
    }

}
