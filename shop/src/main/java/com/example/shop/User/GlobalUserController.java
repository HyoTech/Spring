package com.example.shop.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalUserController {
    @Autowired
    private UserRepository userRepository;

    @ModelAttribute("userLevel")
    public Integer userLevel(Authentication authenticateAction) {
        if (authenticateAction != null && authenticateAction.isAuthenticated()) {
            String username = authenticateAction.getName();
            var result = userRepository.findByUserName(username);
            return result.get().getAuthLevel();
        }
        return null;
    }
}
