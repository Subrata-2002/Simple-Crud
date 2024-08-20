package com.example.simple_crud.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter

@NoArgsConstructor //constructer with all fields
@AllArgsConstructor // constructer with all fields

//create constructor of this class

public class EmployeeDto {
    //instance veriables for employee details

    private long id;
    private String firstName;
    private String lastName;
    private String email;


    @Override
    public String toString() {
        return "EmployeeDto{" +
                "id -" + id +
                ", firstName- '" + firstName + '\'' +
                ", lastName- '" + lastName + '\'' +
                ", email -'" + email + '\'' +
                '}';
    }
}
