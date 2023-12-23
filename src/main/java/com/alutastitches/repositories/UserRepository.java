package com.alutastitches.repositories;

import com.alutastitches.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {

    Optional<UserDetails> findByUsername(String username);
}
