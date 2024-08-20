package com.example.simple_crud.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Null;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

@Getter
@Setter
public class LoginResponse extends MasterResponseBody<String>  {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String token;
    private String refreshToken;


    public LoginResponse(String userNotFound, int i) {
        super(userNotFound, i);
        token= null;
    }
    public LoginResponse(String token,String refreshToken, String message, int status) {
        super(message, status);
        this.token = token;
        this.refreshToken = refreshToken;
    }
}
