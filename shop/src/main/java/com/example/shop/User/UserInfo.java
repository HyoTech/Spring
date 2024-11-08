package com.example.shop.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@ToString
@Getter
@Setter
@NoArgsConstructor
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private String userName;
    private String passWord;
    private String displayName;
    private String picture;
    private Integer authLevel;
    private String email;

    @Enumerated(EnumType.STRING) // Enum 타입은 문자열 형태로 저장해야 함
    private Role role;

    @Builder
    public UserInfo(String userName, String email, String picture, Role role, String displayName) {
        this.userName = userName;
        this.email = email;
        this.displayName = displayName;
        this.picture = picture;
        this.role = role;
    }

    public UserInfo update(String name, String picture) {
        this.userName = name;
        this.picture = picture;
        return this;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }
}
