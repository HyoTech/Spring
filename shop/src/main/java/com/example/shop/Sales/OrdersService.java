package com.example.shop.Sales;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.shop.User.MyUserDetailsService.CustomUser;
import com.example.shop.User.UserInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrdersService {
    private final OrdersRepository ordersRepository;

    public void beOrder(@RequestParam("Count") Integer Count,
            @RequestParam("productName") String productName,
            @RequestParam("Price") Integer Price,
            Authentication auth) {
        Orders orders = new Orders();
        CustomUser user = (CustomUser) auth.getPrincipal();
        UserInfo member = new UserInfo();

        if (Count != null && Price < 100000000) {
            member.setId(user.id);
            orders.setProductName(productName);
            orders.setPrice(Price);
            orders.setCount(Count);
            orders.setMember(member);
        }
        ordersRepository.save(orders);
    }

    public void showOrders(Authentication auth, Model model) {
        List<Orders> result = ordersRepository.customFindAll();
        List<OrderDto> orderDtoList = new ArrayList();

        for (Orders order : result) {
            OrderDto orderDto = new OrderDto();
            orderDto.productName = order.getProductName();
            orderDto.price = order.getPrice();
            orderDto.username = order.getMember().getUserName();
            orderDto.count = order.getCount();
            orderDto.createTime = order.getTime();
            orderDtoList.add(orderDto);
        }
        model.addAttribute("orders", orderDtoList);
    }

    class OrderDto {
        public String productName;
        public Integer price;
        public String username;
        public Integer count;
        public LocalDateTime createTime;
    }
}
