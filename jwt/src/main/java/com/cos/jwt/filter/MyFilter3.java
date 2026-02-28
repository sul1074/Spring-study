package com.cos.jwt.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
@RequiredArgsConstructor
public class MyFilter3 implements Filter {

    private final String secretKey;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        if (req.getMethod().equals("POST")) {
            log.info("POST 요청됨");
            log.info("필터3");
            String headerAuth = req.getHeader("Authorization");
            log.info(headerAuth);

            // 올바른 때만 컨트롤러로 통과시키기
            if (headerAuth == null || !headerAuth.equals(secretKey)) {
                PrintWriter writer = res.getWriter();
                writer.println("인증 안됨");
                return;
            }
        } 

        // 다음 필터로 통과
        chain.doFilter(req, res);
    }
    /**
     * 필터 3는 addFilterBefore로 스프링 시큐리티 필터 걸리기 전에 필터 걸림.
     * 그래서 권한 검증 등등 하기 전에 토큰 유효하지 않으면 바로 돌려보냄
     */
}
