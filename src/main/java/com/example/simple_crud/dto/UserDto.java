package com.example.simple_crud.dto;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
public class UserDto {

    private String username;
    private String email;

}
