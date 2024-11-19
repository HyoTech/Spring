package com.example.shop.User;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.shop.User.MyUserDetailsService.CustomUser;
import com.example.shop.UserAuthSimsa.UserAuthSimsa;
import com.example.shop.UserAuthSimsa.UserAuthSimsaRepository;

import lombok.RequiredArgsConstructor;

@Service
@Controller
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserAuthSimsaRepository userAuthSimsaRepository;

    // 회원가입기능, 폼에 입력된 데이터를 DB에 저장, 패스워드는 암호화
    public void CrtUser(UserInfo userInfo) throws Exception {
        var encoder = new BCryptPasswordEncoder();

        /*
         * if (!userInfo.getPassword()
         * .matches(
         * "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,16}$"))
         * {
         * throw new Exception("비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.");
         * }
         */

        userInfo.setPassword(encoder.encode(userInfo.getPassword()));

        if (userInfo.getAuthLevel() == 3) {
            userInfo.setUserName(userInfo.getUserName());
            userInfo.setPassword(userInfo.getPassword());
            userInfo.setDisplayName(userInfo.getDisplayName());
            userInfo.setEmail(userInfo.getEmail());
            userInfo.setAuthLevel(userInfo.getAuthLevel());
            System.out.println("userInfo=" + userInfo);
            userRepository.save(userInfo);
        } else if (userInfo.getAuthLevel() == 1 || userInfo.getAuthLevel() == 2) {
            UserAuthSimsa userAuthSimsa = new UserAuthSimsa();
            userAuthSimsa.setUserName(userInfo.getUserName());
            userAuthSimsa.setPassword(userInfo.getPassword());
            userAuthSimsa.setDisplayName(userInfo.getDisplayName());
            userAuthSimsa.setEmail(userInfo.getEmail());
            userAuthSimsa.setAuthLevel(userInfo.getAuthLevel());
            System.out.println("userAuthSimsa=" + userAuthSimsa);
            userAuthSimsaRepository.save(userAuthSimsa);
        }
    }

    public void mypages(Authentication auth, Model model) {
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
    }

}
