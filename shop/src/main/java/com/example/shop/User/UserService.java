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
    public void CrtUser(@RequestParam("userName") String userName,
            @RequestParam("password") String password,
            @RequestParam("displayName") String displayName,
            @RequestParam("email") String email,
            @RequestParam("authLevel") Integer authLevel) throws Exception {
        UserInfo userInfo = new UserInfo();
        UserAuthSimsa userAuthSimsa = new UserAuthSimsa();
        var encoder = new BCryptPasswordEncoder();
        var result = userRepository.findByUserName(userName);

        if (userName.length() < 5 || password.length() < 5) {
            throw new Exception("너무 짧은 아이디 혹은 패스워드 입니다.");
        }

        if (result.isPresent()) {
            throw new Exception("중복된 아이디입니다.");
        }

        if (displayName == null) {
            throw new Exception("이름을 입력해주세요.");
        }
        if (authLevel == 3) {
            userInfo.setUserName(userName);
            userInfo.setPassWord(encoder.encode(password));
            userInfo.setDisplayName(displayName);
            userInfo.setEmail(email);
            userInfo.setAuthLevel(authLevel);
            userRepository.save(userInfo);
        } else if (authLevel == 1 || authLevel == 2) {
            userAuthSimsa.setUserName(userName);
            userAuthSimsa.setPassWord(encoder.encode(password));
            userAuthSimsa.setDisplayName(displayName);
            userAuthSimsa.setEmail(email);
            userAuthSimsa.setAuthLevel(authLevel);
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
