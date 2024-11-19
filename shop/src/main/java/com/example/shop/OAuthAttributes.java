package com.example.shop;

import java.util.Map;

import com.example.shop.User.Role;
import com.example.shop.User.UserInfo;

import lombok.Builder;
import lombok.Getter;

@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String picture;
    private String nickname;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email,
            String picture, String nickname) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.nickname = nickname;
    }

    public static OAuthAttributes of(String registrationId, String userNameAttributeName,
            Map<String, Object> attributes) {
        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("email"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .nickname((String) attributes.get("name"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public UserInfo toEntity() {
        return UserInfo.builder()
                .userName(name)
                .email(email)
                .displayName(nickname)
                .password(name)
                .picture(picture)
                .role(Role.USER)
                .build();
    }
}
