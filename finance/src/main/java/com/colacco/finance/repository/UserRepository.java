package com.colacco.finance.repository;

import com.colacco.finance.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByActiveTrue();
}
