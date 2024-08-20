package com.example.simple_crud.controller;

import com.example.simple_crud.dto.EmployeeDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter

@NoArgsConstructor //constructer with all fields
@AllArgsConstructor

public class MasterResponseBody<T> {
//    T empData;
    String message;
    int status;

}
