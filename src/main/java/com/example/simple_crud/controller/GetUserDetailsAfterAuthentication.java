package com.example.simple_crud.controller;

import com.example.simple_crud.dto.UserDto;
import com.example.simple_crud.entity.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetUserDetailsAfterAuthentication extends MasterResponseBody<String>{

    // additional properties to store user details after successful login
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UserDto data;
    private String token;

    public GetUserDetailsAfterAuthentication(UserDto data,String message, int status) {
        super(message, status);
        this.data = data;
    }
    public GetUserDetailsAfterAuthentication(String message, int status){
        super(message, status);
    }

    public GetUserDetailsAfterAuthentication(String Token, String message, int status){
        super(message, status);
        this.token = Token;
    }


}
