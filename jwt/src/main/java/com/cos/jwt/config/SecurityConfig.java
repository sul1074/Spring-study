package com.cos.jwt.config;

import com.cos.jwt.filter.MyFilter1;
import com.cos.jwt.filter.MyFilter3;
import com.cos.jwt.jwt.JwtAuthenticationFilter;
import com.cos.jwt.jwt.JwtAuthorizationFilter;
import com.cos.jwt.jwt.JwtProperties;
import com.cos.jwt.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorsFilter corsFilter;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final UserRepository userRepository;
    private final JwtProperties jwtProperties;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

//        http.addFilterBefore(new MyFilter3(jwtProperties.getSecret()), BasicAuthenticationFilter.class);

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
         * 1. formLogin 비활성화 이유
         * - 세션 기반에서는 스프링 시큐리티가 기본 로그인 페이지를 만들고,
         * 폼 데이터(x-www-form-urlencoded)로 오는 요청을 가로채서 처리했음.
         * - 하지만 우리는 REST API 서버이므로 클라이언트가 JSON 포맷으로 ID/PW를 보낼 것임.
         * formLogin은 JSON 포맷을 이해하지 못하므로 비활성화.
         *
         * 2. 세션/쿠키 방식의 확장성 문제
         * - 쿠키는 기본적으로 동일한 도메인에서만 동작함.
         * - 프론트엔드와 백엔드의 도메인이 다르거나, MSA 환경처럼 서버 도메인이 여러 개로 쪼개지면
         * 크로스 도메인 환경에서 세션 쿠키를 공유하고 전달하기가 매우 까다로워짐 (CORS 및 SameSite 이슈).
         *
         * 3. httpBasic 비활성화 이유
         * - 쿠키 문제를 피하기 위해, 매 API 요청마다 헤더(Authorization)에 ID와 PW를
         * 직접 달아서 보내는 방식이 HTTP Basic 인증 방식임.
         * - 쿠키를 안 쓰니 확장성은 좋지만, 매 요청마다 ID/PW가 네트워크를 타고 날아가므로
         * 중간에 패킷이 털리면 치명적임. (반드시 HTTPS를 강제해야 함)
         *
         * 4. 결론: Bearer (JWT) 방식 사용
         * - 매번 ID/PW를 보내는 Basic 방식의 위험성을 피하기 위해,
         * 최초 로그인 시에만 ID/PW를 보내고, 서버가 증명서(JWT 토큰)를 발급해 줌.
         * - 이후부터는 헤더(Authorization)에 ID/PW 대신 토큰을 담아서 보내는 Bearer 방식을 사용!
         * - 따라서 이 Bearer(JWT) 방식을 적용하기 위해 낡은 formLogin과 httpBasic 설정을 모두 끄는 것임.
         */
        http.formLogin(form -> form.disable());
        http.httpBasic(basic -> basic.disable());

        // jwt 로그인을 위한 필터
        http.addFilter(new JwtAuthenticationFilter(
                jwtProperties,
                authenticationConfiguration.getAuthenticationManager())
        );
        http.addFilter(new JwtAuthorizationFilter(
                jwtProperties,
                userRepository,
                authenticationConfiguration.getAuthenticationManager())
        );

        // 권한 처리
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/join").permitAll()
                .requestMatchers("/api/v1/user/**").hasAnyRole("USER", "MANAGER", "ADMIN")
                .requestMatchers("/api/v1/manager/**").hasAnyRole("MANAGER", "ADMIN")
                .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated() // 다른 요청들은 권한 필요 X
        );

        http.exceptionHandling(exception -> exception
                // 1. 인증되지 않은 사용자 접근 시 (401 Unauthorized)
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("text/plain;charset=UTF-8");
                    response.getWriter().write("인증되지 않은 사용자입니다. (401)");
                })
                // 2. 권한이 없는 사용자 접근 시 (403 Forbidden)
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.setContentType("text/plain;charset=UTF-8");
                    response.getWriter().write("접근 권한이 없습니다. (403)");
                })
        );

        return http.build();
    }
}
