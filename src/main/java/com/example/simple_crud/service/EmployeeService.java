package com.example.simple_crud.service;

import com.example.simple_crud.controller.MasterResponseBody;
import com.example.simple_crud.dto.EmployeeDto;

import java.sql.SQLException;
import java.util.List;

public interface EmployeeService {
    //here the EmployeeDto  is used to transfer data between layers
    MasterResponseBody createEmployee(EmployeeDto employeeDto);

    MasterResponseBody getEmployeeById(Long employeeId);

     List<EmployeeDto> getAllEmployees();

     MasterResponseBody updateEmployee(Long employeeId, EmployeeDto updatedEmployee);

     void deleteEmployee(Long employeeId);

}
