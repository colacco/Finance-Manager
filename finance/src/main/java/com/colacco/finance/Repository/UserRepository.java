package com.colacco.finance.Repository;

import com.colacco.finance.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByActiveTrue();
}
