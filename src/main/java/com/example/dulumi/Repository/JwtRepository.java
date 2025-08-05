package com.example.dulumi.Repository;

import com.example.dulumi.domain.Jwt;
import com.example.dulumi.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JwtRepository extends JpaRepository<Jwt, Long> {
    Optional<Jwt> findByUser(User user);
}
