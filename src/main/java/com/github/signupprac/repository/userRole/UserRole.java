package com.github.signupprac.repository.userRole;

import com.github.signupprac.repository.role.Role;
import com.github.signupprac.repository.users.Users;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_role")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_role_id")
    private Integer userRoleId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
}
