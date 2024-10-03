package com.example.shop.Sales;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Service
@Controller
public class OrdersController {
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
    public String getOrder(Model model) {
        ordersService.showOrders(model);
        return "OrderPage.html";
    }
}
