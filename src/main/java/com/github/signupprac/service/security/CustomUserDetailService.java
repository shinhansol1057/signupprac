package com.github.signupprac.service.security;

import com.github.signupprac.repository.role.Role;
import com.github.signupprac.repository.userDetails.CustomUserDetails;
import com.github.signupprac.repository.userRole.UserRole;
import com.github.signupprac.repository.users.Users;
import com.github.signupprac.repository.users.UsersJpa;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Primary
public class CustomUserDetailService implements UserDetailsService {

    private final UsersJpa usersJpa;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users user = usersJpa.findByEmailFetchJoin(email)
                .orElseThrow(()-> new NullPointerException("email에 해당하는 user를 찾을 수 없습니다."));

        // 해당 user의 email 로 조회한 정보를 CustomUserDetails repository에 빌더로 넣어준다.
        return CustomUserDetails.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getUserRoles().stream().map(UserRole::getRole).map(Role::getName).collect(Collectors.toList()))
                .build();
    }
}
