package com.example.shop.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.ToString;

@Entity
@ToString
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private String userName;
    private String passWord;
    private String displayName;
    private Integer authLevel;

    public String getUserName() {
        return userName;
    }

    public Long getId() {
        return id;
    }

    public String getPassWord() {
        return passWord;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Integer getAuthLevel() {
        return authLevel;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setAuthLevel(Integer authLevel) {
        this.authLevel = authLevel;
    }
}
