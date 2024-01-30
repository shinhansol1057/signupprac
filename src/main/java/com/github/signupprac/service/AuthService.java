package com.github.signupprac.service;

import com.github.signupprac.config.security.JwtTokenProvider;
import com.github.signupprac.repository.role.Role;
import com.github.signupprac.repository.role.RoleJpa;
import com.github.signupprac.repository.userRole.UserRole;
import com.github.signupprac.repository.userRole.UserRoleJpa;
import com.github.signupprac.repository.users.Users;
import com.github.signupprac.repository.users.UsersJpa;
import com.github.signupprac.web.dto.LoginRequest;
import com.github.signupprac.web.dto.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.NotAcceptableStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UsersJpa usersJpa;
    private final RoleJpa roleJpa;
    private final UserRoleJpa userRoleJpa;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional(transactionManager = "tm")
    public boolean signUp(SignUpRequest signUpRequest) {
        if (usersJpa.existsByEmail(signUpRequest.getEmail())) { // 이미 가입된 이메일인지 확인하기
            return false;
        }

        Role role = roleJpa.findByName("USER");

        Users user = Users.builder()
                .name(signUpRequest.getName())
                .nickName(signUpRequest.getNickName())
                .email(signUpRequest.getEmail())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .phoneNum(signUpRequest.getPhoneNum())
                .gender(signUpRequest.getGender())
                .profileImg(signUpRequest.getProfileImg())
                .failureCount(0)
                .status("normal")
                .createAt(LocalDateTime.now())
                .build();
        usersJpa.save(user);
        userRoleJpa.save(
                UserRole.builder()
                        .user(user)
                        .role(role)
                        .build()
        );
        return true;
    }

    public String login(LoginRequest loginRequest) {

        try {
            Authentication authentication =authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            Users user = usersJpa.findByEmailFetchJoin(loginRequest.getEmail())
                    .orElseThrow(() -> new NullPointerException("해당 이메일로 계정을 찾을 수 없습니다."));
            List<String> roles = user.getUserRoles().stream().map(UserRole::getRole).map(Role::getName).toList();

            return jwtTokenProvider.createToken(loginRequest.getEmail(), roles);
        }catch (Exception e) {
            e.printStackTrace();
            throw new NotAcceptableStatusException("로그인 할 수 없습니다.");
        }


    }
}
