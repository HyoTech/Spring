package com.example.shop;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {
        String errorMessage;

        if (exception.getMessage().equalsIgnoreCase("Bad credentials")) {
            errorMessage = "아이디 또는 비밀번호가 잘못되었습니다.";
        } else if (exception.getMessage().equalsIgnoreCase("Username not found")) {
            errorMessage = "존재하지 않는 사용자입니다.";
        } else if (exception.getMessage().equalsIgnoreCase("Authentication failed")) {
            errorMessage = "인증에 실패했습니다.";
        } else {
            errorMessage = "로그인 중 오류가 발생했습니다.";
        }

        request.getSession().setAttribute("errorMessage", errorMessage);
        response.sendRedirect("/login");
    }
}
