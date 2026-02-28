package com.cos.jwt.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.cos.jwt.auth.PrincipalDetails;
import com.cos.jwt.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Date;

/**
 * /login 요청이 오면 이 UsernamePasswordAuthenticationFilter 객체를 상속한 클래스가 동작
 * 근데 formLogin 비활성화 했으므로 동작이 안함.
 * 그래서 addFilter로 추가해줘야 함
 */
@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtProperties jwtProperties;

    public JwtAuthenticationFilter(JwtProperties jwtProperties, AuthenticationManager authenticationManager) {
        super(authenticationManager);
        this.jwtProperties = jwtProperties;
    }

    // /login 요청을 하면 로그인 시도를 위해 실행되는 함수
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            ObjectMapper om = new ObjectMapper();
            User user = om.readValue(request.getInputStream(), User.class);

            log.info("User: {}", user.toString());
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

            /**
             * id, password로 로그인 시도
             * authenticationManager로 로그인 시도 하면 PrincipalDetailsService의 loadUserByUsername 호출됨
             * PrincipalDetails를 세션에 담고 (권한 관리를 위해서) JWT 토큰을 만들어서 반환해주면 됨.
             */
            // PrincipalDetailsService의 loadUserByUsername() 실행됨 -> 즉 토큰을 이용해 로그인 시도해보는 것
            // 제대로 실행되면 로그인이 성공한 것. 이 객체에는 로그인한 유저의 정보가 담기고 스프링 시큐리티 세션에 저장됨
            Authentication authentication =
                    this.getAuthenticationManager().authenticate(authenticationToken);

            // 리턴해주는 이유는 권한 관리를 spring security가 대신 해주기 때문에 편하기 때문
            // 굳이 JWT 토큰을 사용하면서 세션을 만들 이유가 X. 권한 때문에 세션에 넣어주는 것
            return authentication;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 위의 attemptAuthentication 실행 후 인증이 정상적으로 되면 이 함수가 다음에 실행됨
    // 그러면 이 함수에서 JWT 토큰 만들어서 request 요청한 사용자에게 전달해주면 됨
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("로그인 완료되고 successfulAuthentication 호출");
        PrincipalDetails principal = (PrincipalDetails) authResult.getPrincipal();

        // RSA는 아니고 HASH 암호 방식.
        String jwtToken = JWT.create()
                .withSubject(principal.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtProperties.getExpireTime())) // 10분 유효시간
                .withClaim("id", principal.getUser().getId())
                .withClaim("username", principal.getUsername())
                .sign(Algorithm.HMAC512(jwtProperties.getSecret()));

        // 이제 요청한 사용자 응답 헤더에 이 jwt 토큰 값이 Authorization 헤더에 담기게 됨.
        // 그럼 프론트에서 이 값을 브라우저 로컬 스토리지에 저장시켜놔서 요청마다 보내면 됨
        response.addHeader(jwtProperties.getHeaderString(), jwtProperties.getTokenPrefix() + jwtToken);
    }
}
