package com.ck.deskspace.repository;

import com.ck.deskspace.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<User, Long> {
    // Custom method to find a user for login
    Optional<User> findByEmail(String email);
}
