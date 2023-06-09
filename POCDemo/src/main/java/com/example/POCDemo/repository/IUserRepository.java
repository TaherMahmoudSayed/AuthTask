package com.example.POCDemo.repository;

import com.example.POCDemo.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<User,Long> {
    Optional<User> findByuserName(String username);
}
