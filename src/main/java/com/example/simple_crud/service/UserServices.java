package com.example.simple_crud.service;

import com.example.simple_crud.controller.GetUserDetailsAfterAuthentication;
import com.example.simple_crud.controller.MasterResponseBody;
import com.example.simple_crud.controller.TokenInfo;
import com.example.simple_crud.entity.User;

public interface UserServices {
    MasterResponseBody<User> createUser(User user);
// here creteUser method
    MasterResponseBody<String> loginUser(String Logindata, String pwd);

    MasterResponseBody<String> getUserDetails(String token);

    GetUserDetailsAfterAuthentication getFreshToken(String refreshToken);

    TokenInfo tokenInfo(String token);

    MasterResponseBody<String> logoutUser(String token);
}