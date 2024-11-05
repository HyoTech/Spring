package com.example.shop;

import com.example.shop.User.UserInfo;

public class SessionUser {
    private String userName;
    private String email;
    private String picture;

    public SessionUser(UserInfo userInfo) {
        this.userName = userInfo.getUserName();
        this.email = userInfo.getEmail();
        this.picture = userInfo.getPicture();
    }
}
