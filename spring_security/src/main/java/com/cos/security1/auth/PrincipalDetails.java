package com.cos.security1.auth;

import com.cos.security1.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 시큐리티가 /login 주소 요청이 오면 낚아채서 로그인을 진행시킴.
 * 로그인 진행이 완료가 되면 세션을 만들어준다.(Security ContextHolder 세션에 저장)
 * 오브젝트 => Authentication 타입 객체
 * Authentication 안에 User 정보가 있음.
 * User 오브젝트 타입은 UserDetails 타입 객체임
 * <p>
 * 세션 정보를 Security Session에 세션 정보를 저장해두는데
 * 여기 저장될 수 있는 객체는 Authentication 타입이고, 이 안에는 UserDetails 정보가 있음.
 * Security Session => Authentication => UserDetails
 */

@RequiredArgsConstructor
public class PrincipalDetails implements UserDetails {

    private final User user; // 콤포지션 -> 이게 뭔 소리지

    // 해당 User의 권한을 리턴하는 메서드
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });

        return collect;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        // 휴먼 계정이면 false 겠지. 이건 도메인 요구사항에 따라 다름
        return true;
    }
}
