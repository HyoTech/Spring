package com.example.shop.Sales;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.shop.User.MyUserDetailsService.CustomUser;
import com.example.shop.User.UserInfo;
import com.example.shop.User.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrdersService {
    private final OrdersRepository ordersRepository;
    private final UserRepository userRepository;

    public void beOrder(@RequestParam("Count") Integer Count,
            @RequestParam("productName") String productName,
            @RequestParam("Price") Integer Price,
            Authentication auth) {
        Orders orders = new Orders();
        UserInfo member = new UserInfo();
        if (auth != null && auth.isAuthenticated()) {
            Object principal = auth.getPrincipal();
            Optional<UserInfo> userInfoOptional = Optional.empty();

            if (principal instanceof CustomUser) {
                // 커스텀 유저 로그인 사용자
                CustomUser user = (CustomUser) principal;
                member.setId(user.id);
            } else if (principal instanceof OAuth2User) {
                // OAuth2 로그인 사용자
                OAuth2User oAuth2User = (OAuth2User) principal;
                String email = oAuth2User.getAttribute("email"); // OAuth2 사용자의 이메일
                userInfoOptional = userRepository.findByEmail(email);
                member.setId(userInfoOptional.get().getId());
            }
        }

        if (Count != null && Price < 100000000) {
            orders.setProductName(productName);
            orders.setPrice(Price);
            orders.setCount(Count);
            orders.setMember(member);
        }
        ordersRepository.save(orders);
    }

    public void showOrders(Model model) {
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

    // 현재 접속한 멤버의 아이디를 가져와서
    // 해당 아이디가 주문한 주문내역을 출력
    // 그럼 아이디를 어떻게 가져와야 할까?
    // findbymember 함수를 만들어서 현재 로그인한 아이디의 아이디 번호를 받아와서 조회
    public void myOrders(Model model, Authentication auth) {
        if (auth != null && auth.isAuthenticated()) {
            Object principal = auth.getPrincipal();
            Optional<UserInfo> userInfoOptional = Optional.empty();

            if (principal instanceof CustomUser) {
                // 커스텀 유저 로그인 사용자
                CustomUser user = (CustomUser) principal;
                userInfoOptional = userRepository.findByUserName(user.getUsername());

            } else if (principal instanceof OAuth2User) {
                // OAuth2 로그인 사용자
                OAuth2User oAuth2User = (OAuth2User) principal;
                String email = oAuth2User.getAttribute("email"); // OAuth2 사용자의 이메일
                userInfoOptional = userRepository.findByEmail(email);
            }

            if (userInfoOptional.isPresent()) {
                UserInfo userInfo = userInfoOptional.get();
                List<Orders> orders = ordersRepository.findByMember(userInfo);

                model.addAttribute("orders", orders);
            }
        }
    }

    class OrderDto {
        public String productName;
        public Integer price;
        public String username;
        public Integer count;
        public LocalDateTime createTime;
    }
}
