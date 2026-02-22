package com.cos.security1.config.auth;

import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 시큐리티 설정에서 loginProcessingUrl("/login");
 * /login 요청이 오면 자동으로 UserDetailService 타입으로 IoC 되어있는 loadByUsername 함수가 실행됨
 * 함수 파라미터인 username은 로그인할 때 넘겨오는 파라미터 이름과 동일해야 함.
 * 즉, 로그인할 때 파라미터 이름을 username으로 해야 제대로 loadUserByUsername에 파라미터가 전달됨.
 */
@Service
@RequiredArgsConstructor
public class PrincipalDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    // 시큐리티 세션에 저장되는 Authentication 객체 내부에 저장됨
    // 함수 종료 시 @AuthenticationPrincipal 어노테이션이 만들어짐
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다. " + username);
        }
        return new PrincipalDetails(user);
    }
}
