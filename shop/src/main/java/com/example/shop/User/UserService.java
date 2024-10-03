package com.example.shop.User;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;

@Service
@Controller
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    // 회원가입기능, 폼에 입력된 데이터를 DB에 저장, 패스워드는 암호화
    public void CrtUser(@RequestParam("userName") String userName,
            @RequestParam("password") String password,
            @RequestParam("displayName") String displayName) throws Exception {
        UserInfo userInfo = new UserInfo();
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

        userInfo.setUserName(userName);
        userInfo.setPassWord(encoder.encode(password));
        userInfo.setDisplayName(displayName);
        userRepository.save(userInfo);

    }

    // 유저 상세페이지
    public UserDto DetailUser() {
        var a = userRepository.findById(1L);
        var result = a.get();
        var userDto = new UserDto(result.getUserName(), result.getDisplayName());

        if (a.isPresent()) {
            return userDto;
        } else {
            throw new RuntimeException("User not found"); // 예외 처리
        }
    }
}
