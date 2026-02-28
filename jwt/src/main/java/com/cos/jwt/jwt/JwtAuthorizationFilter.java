package com.cos.jwt.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.cos.jwt.auth.PrincipalDetails;
import com.cos.jwt.model.User;
import com.cos.jwt.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;

/**
 * 시큐리티가 filter를 가지고 있는데 그 필터 중에 BasicAuthenticationjFilter 라는 것이 있음
 * 권한이나 인증이 필요한 특정 주소를 요청했을 때 위 필터를 무조건 타게 돼있음
 * 만약 권한이나 인증이 필요한 주소가 아니면 이 필터를 안탐
 */
@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final JwtProperties jwtProperties;
    private final UserRepository userRepository;

    public JwtAuthorizationFilter(JwtProperties jwtProperties, UserRepository userRepository, AuthenticationManager authenticationManager) {
        super(authenticationManager);
        this.jwtProperties = jwtProperties;
        this.userRepository = userRepository;
    }

    // 인증이나 권한이 필요한 API 요청은 이 필터를 탐
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("인증이나 권한이 필요한 요청이 옴");

        String jwtHeader = request.getHeader(jwtProperties.getHeaderString());
        log.info("JWT Header: {}", jwtHeader);

        // 헤더가 있는지 확인
        if (jwtHeader == null || !jwtHeader.startsWith(jwtProperties.getTokenPrefix())) {
            chain.doFilter(request, response);
            return;
        }

        // JWT 토큰 검증. Bearer는 제거하고 가져오기
        String token = request
                .getHeader(jwtProperties.getHeaderString())
                .replace(jwtProperties.getTokenPrefix(), "");

        String username = JWT
                .require(Algorithm.HMAC512(jwtProperties.getSecret())).build()
                .verify(token)
                .getClaim("username")
                .asString();

        // 서명이 정상적으로 인증이 됨
        if (username != null) {
            User user = userRepository.findByUsername(username);

            PrincipalDetails principalDetails = new PrincipalDetails(user);
            
            // JWT 토큰 서명을 통해 서명이 정상이면 Authentication 객체를 만들어줌
            Authentication authentication = 
                    new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());

            // 스프링 시큐리티 세션에 직접 Authentication 객체를 저장해줌
            SecurityContextHolder.getContext().setAuthentication(authentication);

            chain.doFilter(request, response);
        }
    }
}
