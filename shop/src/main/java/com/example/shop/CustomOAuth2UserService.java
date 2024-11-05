package com.example.shop;

import java.util.Collections;
import java.util.NoSuchElementException;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.example.shop.User.UserInfo;
import com.example.shop.User.UserRepository;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
        private final UserRepository userRepository;
        private final HttpSession httpSession;

        @Override
        public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
                OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
                OAuth2User oAuth2User = delegate.loadUser(userRequest);

                String registrationId = userRequest.getClientRegistration().getRegistrationId();
                String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                                .getUserInfoEndpoint().getUserNameAttributeName();

                OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName,
                                oAuth2User.getAttributes());

                UserInfo userInfo = saveOrUpdate(attributes);

                httpSession.setAttribute("user", new SessionUser(userInfo));

                return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(userInfo.getRoleKey())),
                                attributes.getAttributes(),
                                attributes.getNameAttributeKey());
        }

        private UserInfo saveOrUpdate(OAuthAttributes attributes) {
                UserInfo userInfo = userRepository.findByEmail(attributes.getEmail())
                                .map(entity -> entity.update(attributes.getName(), attributes.getPicture()))
                                .orElse(attributes.toEntity());

                return userRepository.save(userInfo);
        }
}
