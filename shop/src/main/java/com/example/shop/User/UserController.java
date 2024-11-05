package com.example.shop.User;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.shop.Sales.OrdersService;
import com.example.shop.User.MyUserDetailsService.CustomUser;

import org.springframework.ui.Model;
import lombok.RequiredArgsConstructor;

@Service
@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;
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
            @RequestParam("authLevel") Integer authLevel) throws Exception {
        userService.CrtUser(userName, password, displayName, authLevel);
        return "redirect:/list/page/1";
    }

    @GetMapping("/login")
    public String login() {
        return "login.html";
    }

    @GetMapping("/my-page")
    public String mypage(Authentication auth, Model model) {
        if (auth != null) {
            Object principal = auth.getPrincipal();

            if (principal instanceof CustomUser) {
                // 일반 로그인 사용자
                CustomUser user = (CustomUser) principal;
                Optional<UserInfo> userInfo = userRepository.findByUserName(user.getUsername());
                userInfo.ifPresent(info -> model.addAttribute("userinfo", info));

            } else if (principal instanceof OAuth2User) {
                // OAuth2 사용자
                OAuth2User oAuth2User = (OAuth2User) principal;
                String email = oAuth2User.getAttribute("email"); // Google의 경우 이메일을 사용
                Optional<UserInfo> userInfo = userRepository.findByEmail(email);
                userInfo.ifPresent(info -> model.addAttribute("userinfo", info));
            }
        }
        ordersService.myOrders(model, auth);
        return "mypage.html";
    }

}
