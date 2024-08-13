package com.example.shop.User;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // DB에서 username을 가진 유저를 찾아와서
        // return new User(유저아이디, 비번, 권한) 해주세요
        var result = userRepository.findByUserName(username);
        var user = result.get();
        if (result.isEmpty()) {
            throw new UsernameNotFoundException("등록된 아이디 없음");
        }

        List<GrantedAuthority> auth = new ArrayList<>();
        auth.add(new SimpleGrantedAuthority("일반유저"));

        return new User(user.getUserName(), user.getPassWord(), auth);
    }
}
