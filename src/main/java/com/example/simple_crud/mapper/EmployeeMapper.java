package com.example.simple_crud.mapper;

/* mapper class to convert entity to D */
import com.example.simple_crud.dto.EmployeeDto;
import com.example.simple_crud.entity.Employee;


public class EmployeeMapper {

    public static EmployeeDto mapToEmployeeDto(Employee employee) {
        //here mapToEmployeeDto method maps employeejpa entity to EmployeeDto DTO
        return new EmployeeDto(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail()
        );
    }

// here create another method to map EmployeeDto to EmployeeJpa entity
public static Employee mapToEmployee(EmployeeDto employeeDto) {
    return new Employee(
            employeeDto.getId(),
            employeeDto.getFirstName(),
            employeeDto.getLastName(),
            employeeDto.getEmail()
    );
}

}