package com.github.signupprac.web.controller;

import com.github.signupprac.service.AuthService;
import com.github.signupprac.web.dto.LoginRequest;
import com.github.signupprac.web.dto.SignUpRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SignController {

    private final AuthService authService;

    @PostMapping("/sign-up")
    public String register(@RequestBody SignUpRequest signUpRequest) {
        boolean isSuccess = authService.signUp(signUpRequest);
        return isSuccess ? "회원가입 성공" : "회원가입 실패";
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest, HttpServletResponse httpServletResponse) {
        String token = authService.login(loginRequest);
        httpServletResponse.setHeader("Token", token);
        return "로그인이 성공하였습니다.";
    }
}
