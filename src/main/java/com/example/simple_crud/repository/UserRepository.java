package com.example.simple_crud.repository;

import com.example.simple_crud.entity.Employee;
import com.example.simple_crud.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String email);
}
