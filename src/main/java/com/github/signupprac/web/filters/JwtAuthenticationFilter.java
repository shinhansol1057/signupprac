package com.github.signupprac.web.filters;

import com.github.signupprac.config.security.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String jwtToken = jwtTokenProvider.resolveToken(request); // Token 에서 원하는 정보를 가져오기

        if (jwtToken != null && jwtTokenProvider.validateToken(jwtToken)) { // jwtToken 이 존재하고 유효하다면
            Authentication auth = jwtTokenProvider.getAuthentication(jwtToken); // jwtTokenProvider 에서 권한을 가져오고
            SecurityContextHolder.getContext().setAuthentication(auth); // SecurityContextHolder.getContext() 에 auth 를 넣어준다.
        }

        filterChain.doFilter(request, response);
    }
}
