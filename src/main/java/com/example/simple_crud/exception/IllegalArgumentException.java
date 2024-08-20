package com.example.simple_crud.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class IllegalArgumentException extends RuntimeException{
    public IllegalArgumentException(String message){
        super(message);
        System.out.println(message);
    }
}
