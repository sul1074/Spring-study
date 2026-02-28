package com.cos.jwt.controller;

import com.cos.jwt.UserService;
import com.cos.jwt.auth.PrincipalDetails;
import com.cos.jwt.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RestApiController {

    private final UserService userService;

    @GetMapping("/home")
    public String home() {
        return "<h1>home</h1>";
    }

    @PostMapping("/token")
    public String token() {
        return "<h1>token</h1>";
    }

    @GetMapping("/api/v1/user")
    public String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return "user";
    }

    @GetMapping("/api/v1/manager")
    public String manager() {
        return "mananger";
    }

    @GetMapping("/api/v1/admin")
    public String admin() {
        return "admin";
    }

    @PostMapping("/join")
    public String join(@RequestBody User user) {
        userService.join(user);

        return "회원가입 완료";
    }
}
