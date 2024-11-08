package com.example.shop.User;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.shop.User.MyUserDetailsService.CustomUser;

@ControllerAdvice
public class GlobalUserController {
    @Autowired
    private UserRepository userRepository;

    @ModelAttribute("userLevel")
    public Integer userLevel(Authentication authenticateAction) {

        if (authenticateAction != null && authenticateAction.isAuthenticated()) {
            String username = authenticateAction.getName();
            var result = userRepository.findByUserName(username);
            if (result.isPresent()) {
                System.out.println(result.get().getAuthLevel());
                return result.get().getAuthLevel();
            }
        }

        return null;
    }

    @ModelAttribute("ROLE")
    public Role userROLE(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            String username = null;

            // OAuth2 로그인인 경우 email 속성을 username으로 사용
            if (principal instanceof OAuth2User) {
                username = ((OAuth2User) principal).getAttribute("email"); // 이메일 사용
            } else if (principal instanceof CustomUser) {
                username = ((CustomUser) principal).getUsername();
            }

            if (username != null) {
                Optional<UserInfo> result = userRepository.findByUserName(username);
                if (result.isPresent()) {
                    return result.get().getRole();
                }
            }
        }
        return null;
    }

    @ModelAttribute("username")
    public String username(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof OAuth2User) {
                return ((OAuth2User) principal).getAttribute("name"); // OAuth2 사용자라면 name 속성 사용
            } else if (principal instanceof CustomUser) {
                return ((CustomUser) principal).getUsername(); // 일반 사용자라면 username 속성 사용
            }
        }
        return null;
    }
}
