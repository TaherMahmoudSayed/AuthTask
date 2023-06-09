package com.example.POCDemo.repository;

import com.example.POCDemo.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IRoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRole(String rolename);
}
