package com.example.shop.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    private String userName;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    // @Column(name = "passWord")
    private String password;
    private String displayName;
    private String picture;
    private Integer authLevel;

    @Pattern(regexp = "^(?:\\w+\\.?)*\\w+@(?:\\w+\\.)+\\w+$", message = "이메일 형식이 올바르지 않습니다.")
    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    private String email;

    @Enumerated(EnumType.STRING) // Enum 타입은 문자열 형태로 저장해야 함
    private Role role;

    @Builder
    public UserInfo(String userName, String email, String picture, Role role, String displayName, String password) {
        this.userName = userName;
        this.email = email;
        this.displayName = displayName;
        this.picture = picture;
        this.role = role;
        this.password = password;
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
