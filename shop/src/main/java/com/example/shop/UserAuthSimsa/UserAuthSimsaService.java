package com.example.shop.UserAuthSimsa;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.shop.User.UserInfo;
import com.example.shop.User.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@Controller
@RequiredArgsConstructor
public class UserAuthSimsaService {
    private final UserRepository userRepository;
    private final UserAuthSimsaRepository userAuthSimsaRepository;

    public void viewAuthSimsa(Model model) {
        List<UserAuthSimsa> result = userAuthSimsaRepository.findAll();
        List<UserAuthSimsaDto> userauthDtoList = new ArrayList();

        for (UserAuthSimsa uasimsa : result) {
            UserAuthSimsaDto userAuthDto = new UserAuthSimsaDto();
            userAuthDto.id = uasimsa.getId();
            userAuthDto.userName = uasimsa.getUserName();
            userAuthDto.displayName = uasimsa.getDisplayName();
            userAuthDto.email = uasimsa.getEmail();
            userAuthDto.authLevel = uasimsa.getAuthLevel();
            userauthDtoList.add(userAuthDto);
        }
        model.addAttribute("usAuthSimsa", userauthDtoList);
    }

    public Optional<String> approveAuth(@PathVariable("id") Long id,
            Authentication auth) {
        Optional<UserAuthSimsa> authResult = userAuthSimsaRepository.findById(id);

        if (authResult.isPresent() && auth.isAuthenticated()) {
            UserAuthSimsa authData = authResult.get();

            // userRepository에서 userName으로 검색하여 중복 여부 검사
            Optional<UserInfo> existingUserInfo = userRepository.findByUserName(authData.getUserName());

            if (existingUserInfo.isPresent()) {
                // 동일한 userName을 가진 유저가 존재하면 반려
                return Optional.of("해당 유저 이름은 이미 존재합니다.");
            }

            UserInfo userInfo = new UserInfo();
            userInfo.setId(authData.getId());
            userInfo.setUserName(authData.getUserName());
            userInfo.setPassWord(authData.getPassWord());
            userInfo.setDisplayName(authData.getDisplayName());
            userInfo.setEmail(authData.getEmail());
            userInfo.setAuthLevel(authData.getAuthLevel());

            userRepository.save(userInfo);
            userAuthSimsaRepository.deleteById(id);
        }
        return Optional.of("해당 id의 승인 요청이 존재하지 않습니다.");
    }

    public void rejectAuth(@PathVariable("id") Long id, Authentication auth) {
        Optional<UserAuthSimsa> result = userAuthSimsaRepository.findById(id);
        if (result.isPresent() && auth.isAuthenticated()) {
            userAuthSimsaRepository.deleteById(id);
        }
    }

    class UserAuthSimsaDto {
        public Long id;
        public String userName;
        public String displayName;
        public String email;
        public Integer authLevel;
    }
}
