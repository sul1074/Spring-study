package com.cos.jwt.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * /login 요청이 오면 이 UsernamePasswordAuthenticationFilter 객체를 상속한 클래스가 동작
 * 근데 formLogin 비활성화 했으므로 동작이 안함.
 * 그래서 addFilter로 추가해줘야 함
 */
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    // /login 요청을 하면 로그인 시도를 위해 실행되는 함수
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("로그인 시도");

        /**
         * id, password로 로그인 시도
         * authenticationManager로 로그인 시도 하면 PrincipalDetailsService의 loadUserByUsername 호출됨
         * PrincipalDetails를 세션에 담고 (권한 관리를 위해서) JWT 토큰을 만들어서 반환해주면 됨.
         */
        
        return super.attemptAuthentication(request, response);
    }
}
