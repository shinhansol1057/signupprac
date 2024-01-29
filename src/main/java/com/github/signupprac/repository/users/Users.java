package com.github.signupprac.repository.users;

import com.github.signupprac.repository.userRole.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@EqualsAndHashCode(of = "userId")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "nick_name", nullable = false)
    private String nickName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "phone_num", nullable = false)
    private String phoneNum;

    @Column(name = "gender", nullable = false)
    private String gender;

    @Column(name = "profile_img")
    private String profileImg;

    @Column(name = "failure_count", nullable = false, columnDefinition = "DEFAULT 0")
    private Integer failureCount;

    @Column(name = "status", nullable = false, columnDefinition = "DEFAULT 'normal'")
    private String status;

    @Column(name = "create_at", nullable = false)
    private LocalDateTime createAt;

    @Column(name = "delete_at")
    private LocalDateTime deleteAt;

    @Column(name = "update_at")
    private LocalDateTime updateAt;

    @Column(name = "lock_at")
    private LocalDateTime lockAt;

    @OneToMany(mappedBy = "user")
    private List<UserRole> userRoles;
}
