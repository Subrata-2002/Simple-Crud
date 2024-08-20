package com.example.simple_crud.controller;

import com.example.simple_crud.dto.EmployeeDto;
import com.example.simple_crud.service.EmployeeService;

import lombok.AllArgsConstructor;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
//import org.slf4j.Logger;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor

@RestController // Rest controller is used to handle HTTP requests
@RequestMapping("/api/employee")
@AllArgsConstructor
public class EmployeeController{

    @Autowired
    private EmployeeService employeeService;

//build add Employee Rest Api
    @PostMapping()
    public ResponseEntity<MasterResponseBody> createEmployee(@RequestBody EmployeeDto employeeDto){
        System.out.println("In addEmployee");
        MasterResponseBody savedEmployee = employeeService.createEmployee(employeeDto);
        System.out.println("emp is  "+savedEmployee.toString());
        HttpStatus status;
        switch (savedEmployee.getStatus()) {
            case 400:
                status = HttpStatus.BAD_REQUEST;
                break;
            case 404:
                status = HttpStatus.NOT_FOUND;
                break;
            case 501:
                status = HttpStatus.NOT_IMPLEMENTED;
                break;
            default:
                status = HttpStatus.OK;
        }

        // Return the ResponseEntity with the status and body
        return new ResponseEntity<>(savedEmployee, status);

    }

    @GetMapping("{id}")
    public ResponseEntity<MasterResponseBody> getEmployeeById(@PathVariable("id") Long employeeId){
        MasterResponseBody data = employeeService.getEmployeeById(employeeId);
        HttpStatus status;
        switch (data.getStatus()) {
            case 404:
                status = HttpStatus.NOT_FOUND;
                break;
            case 501:
                status = HttpStatus.NOT_IMPLEMENTED;
                break;
            default:
                status = HttpStatus.OK;
        }

        // Return the ResponseEntity with the status and body
        return new ResponseEntity<>(data, status);
    }

    @GetMapping
    public ResponseEntity<List<EmployeeDto>> getAllEmployees(){
        List<EmployeeDto> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }

    @PostMapping("/update")
    public ResponseEntity<MasterResponseBody> updateEmployee(@RequestBody EmployeeDto updatedEmployee){
//        final Logger logger = (Logger) LoggerFactory.getLogger(EmployeeServiceImpl.class);

        MasterResponseBody masterResponseBody = employeeService.updateEmployee(updatedEmployee.getId(),updatedEmployee);
        HttpStatus status;
        switch (masterResponseBody.getStatus()) {
            case 404:
                status = HttpStatus.NOT_FOUND;
                break;
            case 501:
                status = HttpStatus.NOT_IMPLEMENTED;
                break;
            default:
                status = HttpStatus.OK;
        }

        // Return the ResponseEntity with the status and body
        return new ResponseEntity<>(masterResponseBody, status);
//        ResponseEntity.
    }

    //Build Delete Employee Rest Api
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable("id") Long employeeId){
        System.out.println("deleted successfully"+employeeId);
        employeeService.deleteEmployee(employeeId);
        return ResponseEntity.ok("Employee with id "+employeeId+" deleted successfully");
    }

}
