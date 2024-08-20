package com.example.simple_crud.service.impl;
import com.example.simple_crud.controller.MasterResponseBody;
import com.example.simple_crud.dto.EmployeeDto;
import com.example.simple_crud.entity.Employee;
import com.example.simple_crud.exception.IllegalArgumentException;
import com.example.simple_crud.exception.ResourceNotFoundException;
import com.example.simple_crud.mapper.EmployeeMapper;
import com.example.simple_crud.mapper.Test;
import com.example.simple_crud.repository.EmployeeRepository;
import com.example.simple_crud.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;

    @Override
    public MasterResponseBody createEmployee(EmployeeDto employeeDto){
        try{
            if(employeeDto.getEmail() == "" || employeeDto.getFirstName() == ""){
                throw new IllegalArgumentException("Email and first name cannot be null :)");
            }

        Optional<Employee> existingEmp = employeeRepository.findByEmail(employeeDto.getEmail());
            System.out.println("email is - "+employeeDto.getEmail());
        if (existingEmp.isPresent()) {
            throw new ResourceNotFoundException("Email already exists, please select a different email.");
        }

        Employee employee = EmployeeMapper.mapToEmployee(employeeDto);
        //here we convert the employeeDto entity to employeeJpa entity
        //now lets save the employeeJpa entity to the database
      Employee savedEmployee =  employeeRepository.save(employee);// here we pass the employeeJpa entity to the repository save method

//            return new MasterResponseBody(EmployeeMapper.mapToEmployeeDto(savedEmployee), "Employee Created",201);
            return new MasterResponseBody( "Employee Created",200);
        }catch (IllegalArgumentException e) {
            // Handle validation errors
            MasterResponseBody mb = new MasterResponseBody();
            mb.setMessage(e.getMessage());
            mb.setStatus(400); // Bad Request status code
            return mb;
        }catch(ResourceNotFoundException e){
            MasterResponseBody mb=new MasterResponseBody();
            mb.setMessage(e.getMessage());
            mb.setStatus(404);
            return mb;
        }catch (Exception e) {
            // Handle any other exceptions
            MasterResponseBody mb = new MasterResponseBody();
            mb.setMessage("An unexpected error occurred: " + e.getMessage());
            mb.setStatus(501);
            return mb;
        }
    }

    @Override
    public MasterResponseBody getEmployeeById(Long employeeId) {
        try{
            Employee employee = employeeRepository.findById(employeeId)
                    .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + employeeId));
            // Map the employee to DTO
//            return new MasterResponseBody(EmployeeMapper.mapToEmployeeDto(employee), "Employee found", 200);
            return new MasterResponseBody<>("Employee found", 200);
        }catch (ResourceNotFoundException e) {
            // Handle the case where the employee is not found
            MasterResponseBody mb = new MasterResponseBody();
            mb.setMessage(e.getMessage());
            mb.setStatus(404);
            return mb;
        } catch (Exception e) {
            // Handle any other exceptions
            MasterResponseBody mb = new MasterResponseBody();
            mb.setMessage("An unexpected error occurred: " + e.getMessage());
            mb.setStatus(501);
            return mb;
        }

    }

    @Override
    public List<EmployeeDto> getAllEmployees() {
       List<Employee> employees = employeeRepository.findAll();
       return employees.stream().map((employee) -> EmployeeMapper.mapToEmployeeDto(employee))
               .collect(Collectors.toList());
    }

    @Override
    public MasterResponseBody updateEmployee(Long employeeId, EmployeeDto updatedEmployee) {
        try{
            Employee employee=new Employee();
            employee =  employeeRepository.findById(employeeId).orElseThrow(() -> new ResourceNotFoundException("Employee is not exist with id " + employeeId));

            Optional<Employee> existingEmployee = employeeRepository.findByEmail(updatedEmployee.getEmail());//body email

            if(existingEmployee.isPresent()){

            }
//            System.out.println("email id is - "+updatedEmployee.getEmail());
//            System.out.println("existing employee id is - "+employeeRepository.findByEmail(updatedEmployee.getEmail()));
//            System.out.println("id is "+existingEmployee.get().getId()+" and "+employeeId);
//            System.out.println("check"+existingEmployee.isPresent());

            if (existingEmployee.isPresent() && (existingEmployee.get().getId() != employeeId)) {
                throw new ResourceNotFoundException("Email already exist! Please use a different email :)");
            }

            employee.setFirstName(updatedEmployee.getFirstName());
            employee.setLastName(updatedEmployee.getLastName());
            employee.setEmail(updatedEmployee.getEmail());

            Test t=new Test(5);

            System.out.println("Test class - "+t.getI());



            Employee updatedEmployeeObj = employeeRepository.save(employee);
            System.out.println("Updated employee: " + updatedEmployeeObj);
//            return EmployeeMapper.mapToEmployeeDto(updatedEmployeeObj);

//            return new MasterResponseBody(EmployeeMapper.mapToEmployeeDto(updatedEmployeeObj), "Employee updated",200);
            return new MasterResponseBody("Employee updated",200);
        }
        catch (ResourceNotFoundException e){
            System.out.println("djsgfkjsdgfkjdsfg");
            System.out.println("djsgfkjsdgfkjdsfg - > "+ e.getMessage());

            MasterResponseBody mb=new MasterResponseBody();
            mb.setMessage(e.getMessage());
            mb.setStatus(404);
            return mb;
        }catch (Exception e){
            MasterResponseBody mb=new MasterResponseBody();
            mb.setMessage("An unexpected error occurred: " + e.getMessage());
            mb.setStatus(501);
            return mb;
        }
    }

    @Override
    public void deleteEmployee(Long employeeId) {
        Employee employee =  employeeRepository.findById(employeeId).orElseThrow(
                () -> new ResourceNotFoundException("Employee is not exist with id "+employeeId)
        );
        employeeRepository.deleteById(employeeId);
    }

}
