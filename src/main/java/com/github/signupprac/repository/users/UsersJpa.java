package com.github.signupprac.repository.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersJpa extends JpaRepository<Users, Integer> {

    @Query("SELECT u FROM Users u JOIN FETCH u.userRoles ur JOIN FETCH ur.role WHERE u.email = ?1")
    Optional<Users> findByEmailFetchJoin(String email);

    boolean existsByEmail(String email);
}
