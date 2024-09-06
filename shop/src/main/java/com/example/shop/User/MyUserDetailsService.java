package com.example.shop.User;

import java.util.ArrayList;
import java.util.Collection;
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
        // customUser 클래스 추가 후 기존 user클래스에 있던 내용 플러스 알파로 사용자 이름과 아이디 받아오는 기능 추가
        var result = userRepository.findByUserName(username);
        var user = result.get();
        List<GrantedAuthority> auth = new ArrayList<>();

        // 등록된 아이디가 없는 경우 예외처리
        if (result.isEmpty()) {
            throw new UsernameNotFoundException("등록된 아이디 없음");
        }

        // 유저 권한 메모
        auth.add(new SimpleGrantedAuthority("일반유저"));

        // customUser클래스의 데이터 받아오기
        CustomUser customUser = new CustomUser(user.getUserName(), user.getPassWord(), auth);
        customUser.id = user.getId();
        customUser.displayName = user.getDisplayName();

        return customUser;
    }

    public class CustomUser extends User {
        public long id;
        public String displayName;

        public CustomUser(String username,
                String password,
                Collection<? extends GrantedAuthority> authorities) {
            super(username, password, authorities);
            // TODO Auto-generated constructor stub
        }

    }
}
