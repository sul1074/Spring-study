package com.cos.jwt.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorsFilter corsFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        /**
         * csrf 해제: 세션을 안쓰니깐 방어 필요 없음
         * CSRF = 크로스 사이트 요청 위조
         * 세션은 요청마다 무조건 쿠키(세션 ID)를 보내야 함.
         * 이를 노려서 세션 쿠키를 발급받고 요청은 잘못된 API 요청을 보내는 것
         * JWT는 자동으로 안가지고, 프론트엔드 개발자가 직접 브라우저에서 꺼내서 보내야 함
         */
        http.csrf(csrf -> csrf.disable());

        // JWT 기반이므로 무상태로 -> 세션을 안쓰겠다는 소리
        http.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        // @CrossOrigin(인증X), 시큐리티 필터에 등록 인증 (O)
        // jwt는 토큰 없이 보내면 컨트롤러에 못가게 컷트 하므로 cors 에러가 떠버리므로 필터 추가
        // 즉, 인증은 실패해도 cors는 허용되었다는 것을 알려주기 위함
        http.addFilter(corsFilter);

        /**
         * JWT 필터가 대신 할거므로 비활성화
         * 세션 때는 스프링 시큐리티가 알아서 로그인 페이지 하고 로그인 요청도 가로채서 대신 해줬음 막
         * 근데 이제 로그인 요청은 JSON으로 올건데 (REST API), formLogin은 JSON 포맷 이해 못함.
         */
        http.formLogin(form -> form.disable());
        http.httpBasic(basic -> basic.disable());

        // 권한 처리
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/user/**").hasAnyRole("USER", "MANAGER", "ADMIN")
                .requestMatchers("/api/v1/manager/**").hasAnyRole("MANAGER", "ADMIN")
                .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                .anyRequest().permitAll() // 다른 요청들은 권한 필요 X
        );

        return http.build();
    }
}
