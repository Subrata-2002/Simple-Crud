package com.example.simple_crud.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenInfo extends MasterResponseBody<String>{

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String uData;

    public TokenInfo(String message, int status){
        super(message, status);
    }

    public TokenInfo(String uData, String message, int status) {
        super(message, status);
        this.uData = uData;
    }
}
