package com.example.shop.Sales;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.shop.User.UserInfo;
import com.example.shop.User.MyUserDetailsService.CustomUser;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Service
@Controller
public class OrdersController {
    private final OrdersRepository ordersRepository;
    private final OrdersService ordersService;

    @PostMapping("/order")
    public String postOrder(@RequestParam("Count") Integer Count,
            @RequestParam("productName") String productName,
            @RequestParam("Price") Integer Price,
            Authentication auth) {
        ordersService.beOrder(Count, productName, Price, auth);
        return "redirect:/list/page/1";
    }

    @GetMapping("/order/all")
    public String getOrder(Authentication auth, Model model) {
        ordersService.showOrders(auth, model);
        return "OrderPage.html";
    }
}
