package com.example.simple_crud.repository;

import com.example.simple_crud.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.SQLException;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee,Long> {
        Optional<Employee> findByEmail(String email);
}
