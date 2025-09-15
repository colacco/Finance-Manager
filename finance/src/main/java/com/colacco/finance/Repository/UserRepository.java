package com.colacco.finance.Repository;

import com.colacco.finance.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
