package com.github.signupprac.web.direction;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/exceptions")
public class ExceptionController {

    @GetMapping(value = "/entrypoint")
    public void entrypointException() {
        throw new NullPointerException("로그인이 필요합니다");

    }

    @GetMapping(value = "/access-denied")
    public void accessDeniedException() {
        throw new NullPointerException("권한이 없습니다.");
    }
}
