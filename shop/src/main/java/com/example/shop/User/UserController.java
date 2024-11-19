package com.example.shop.User;

import java.util.Map;

import org.springframework.boot.autoconfigure.integration.IntegrationProperties.Error;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;

import com.example.shop.Sales.OrdersService;

import jakarta.validation.Valid;

import org.springframework.ui.Model;

import lombok.RequiredArgsConstructor;

@Service
@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final OrdersService ordersService;

    @GetMapping("/UserInput")
    public String UsrInputForm(Authentication auth, Model model) {
        if (auth != null && auth.isAuthenticated()) {
            return "redirect:/list";
        }
        model.addAttribute("userInfo", new UserInfo());
        return "CreateUser.html";
    }

    @PostMapping("/CreateUser")
    public String CreateUser(@Valid @ModelAttribute("userInfo") UserInfo userInfo,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) throws Exception {
        System.out.println("Received userInfo=" + userInfo);
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userInfo",
                    bindingResult);
            redirectAttributes.addFlashAttribute("userInfo", userInfo);
            return "CreateUser";
        }

        userService.CrtUser(userInfo);
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
