package com.cos.jwt.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "jwt") // yml에서 jwt로 시작하는 상수 찾아서 자동 주입
public class JwtProperties {
    private String secret;
    private int expireTime = 60000 * 10;
    private String tokenPrefix = "Bearer ";
    private String headerString = "Authorization";
}