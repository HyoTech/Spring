package com.example.shop.UserAuthSimsa;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Service
@Controller
@RequiredArgsConstructor
public class UserAuthSimsaController {
    private final UserAuthSimsaService userAuthSimsaService;

    @GetMapping("/authsimsa")
    public String userAuthSimsa(Model model) {
        userAuthSimsaService.viewAuthSimsa(model);
        return "authSimsa.html";
    }

    // 수락 버튼이 눌려진다면, 임시 테이블에 있던 데이터가 정규 테이블로 이동
    // 그리고 이동이 완료되면 임시 테이블에 있던 데이터는 삭제
    @PostMapping("/approve/{id}")
    ResponseEntity<String> approveAuth(@PathVariable("id") Long id, Authentication auth) {
        userAuthSimsaService.approveAuth(id, auth);
        return ResponseEntity.ok("승인되었습니다.");
    }

    @DeleteMapping("/reject/{id}")
    ResponseEntity<String> deleteUserAuth(@PathVariable("id") Long id, Authentication auth) {
        userAuthSimsaService.rejectAuth(id, auth);
        return ResponseEntity.ok("반려되었습니다.");
    }

    class UserAuthSimsaDto {
        public Long id;
        public String userName;
        public String displayName;
        public String email;
        public Integer authLevel;
    }
}
