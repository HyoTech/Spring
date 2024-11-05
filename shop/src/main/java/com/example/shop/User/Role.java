package com.example.shop.User;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    ADMIN("ROLE_ADMIN", "관리자"),
    Seller("ROLE_SELLER", "판매자"),
    USER("ROLE_USER", "일반사용자");

    private final String key;
    private final String title;
}
