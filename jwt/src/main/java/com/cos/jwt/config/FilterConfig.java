package com.cos.jwt.config;

import com.cos.jwt.filter.MyFilter1;
import com.cos.jwt.filter.MyFilter2;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<MyFilter1> myFilter1() {
        FilterRegistrationBean<MyFilter1> bean = new FilterRegistrationBean<>(new MyFilter1());
        bean.addUrlPatterns("/*");
        bean.setOrder(0); // 낮은 번호가 필터중에서 가장 먼저 실행됨

        return bean;
    }

    @Bean
    public FilterRegistrationBean<MyFilter2> myFilter2() {
        FilterRegistrationBean<MyFilter2> bean = new FilterRegistrationBean<>(new MyFilter2());
        bean.addUrlPatterns("/*");
        bean.setOrder(1); // 낮은 번호가 필터중에서 가장 먼저 실행됨

        return bean;
    }

    /**
     * 스프링 시큐리티 필터의 기본 우선순위는 -100임
     * 이러면 필터1 -> 필터2 순서로 적용됨. 그리고 Spring Security 필터가 다 적용되고 나서 적용됨 얘네들
     * 만약 스프링 시큐리티 필터보다 먼저 적용되게 하고 싶다면, 별도 클래스 객체로 만든 후 스프링 시큐리티에서 addFilterBefore로 추가해주면 됨 
     */
}
