package com.example.shop.User;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@Controller
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    // 회원가입기능, 폼에 입력된 데이터를 DB에 저장, 패스워드는 암호화
    public void CrtUser(String username, String password, String name) throws Exception {
        UserInfo userInfo = new UserInfo();
        var encoder = new BCryptPasswordEncoder();
        var result = userRepository.findByUserName(username);

        if (username.length() < 8 || password.length() < 8) {
            throw new Exception("너무 짧은 아이디 혹은 패스워드 입니다.");
        }

        if (result.isPresent()) {
            throw new Exception("중복된 아이디입니다.");
        }
        userInfo.setUserName(username);
        userInfo.setPassWord(encoder.encode(password));
        userInfo.setDisplayName(name);
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
