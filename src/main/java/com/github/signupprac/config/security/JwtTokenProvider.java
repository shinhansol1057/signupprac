package com.github.signupprac.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final UserDetailsService userDetailsService;

    @Value("${jwtpassword.source}")
    private String secretKey;

    private String key;

    @PostConstruct
    public void setUp(){ // JWT_SECRET_KEY를 인코딩
        key = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    private long tokenValidMillisecond = 1000L * 60 * 60; // Token 유호 시간 : 1시간



    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("Token");
    }

    public String createToken(String email, List<String> roles) { // 토큰 생성
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("roles", roles);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidMillisecond))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    public boolean validateToken(String jwtToken) {
        try {
            Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(jwtToken).getBody();
            Date now = new Date();
            return !claims.getExpiration().before(now);
        }catch (Exception e) {
            return false;
        }
    }

    public Authentication getAuthentication(String jwtToken) {
        String email = Jwts.parser().setSigningKey(key).parseClaimsJws(jwtToken).getBody().getSubject();
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private String getUserEmail(String jwtToken) {
        return Jwts.parser().setSigningKey(key).parseClaimsJws(jwtToken).getBody().getSubject();
    }
}
