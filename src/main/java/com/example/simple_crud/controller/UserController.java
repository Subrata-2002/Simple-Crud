package com.example.simple_crud.controller;
import com.example.simple_crud.dto.LoginDto;
import com.example.simple_crud.entity.User;
import com.example.simple_crud.logout.BlackList;
import com.example.simple_crud.service.UserServices;
import com.example.simple_crud.utils.JwtUtil;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@RestController
@RequestMapping("/api/user")

public class UserController {

  @Autowired
  private UserServices userService;

  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  private JwtUtil jwtUtil;

  @Autowired
  private BlackList blackList;

    @PostMapping
    public ResponseEntity<MasterResponseBody<User>> createUser(@RequestBody User user){
        // Validate user data before saving
            MasterResponseBody<User> savedUser = userService.createUser(user);
        HttpStatus status;
        switch (savedUser.getStatus()) {
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
           return new ResponseEntity<>(savedUser, status);
    }


  @PostMapping("/login")
  public ResponseEntity<MasterResponseBody<String>> loginUser(@RequestBody LoginDto logindto){
//        String token = String.valueOf(userService.loginUser(logindto.getLogindata(), logindto.getPwd()));
      System.out.println("welcome to login");
      MasterResponseBody<String> bb= userService.loginUser(logindto.getLogindata(), logindto.getPwd());
    return new ResponseEntity<>(bb, HttpStatusCode.valueOf(bb.getStatus()));
  };

    @GetMapping("/profile")
    public ResponseEntity<MasterResponseBody<String>> getUserDetails(@RequestHeader("Authorization") String authorizationHeader) {

        String token = extractToken(authorizationHeader);
        MasterResponseBody<String> userDetails = userService.getUserDetails(token);
//        HttpStatus status = HttpStatus.valueOf(userDetails.getStatus());
        System.out.println("welcome to profile and the details "+userDetails.toString());
        HttpStatus status = HttpStatus.valueOf(userDetails.getStatus());
        return new ResponseEntity<>(userDetails, status);
    }

    public String extractToken(String authorizationHeader) {
        if (authorizationHeader != "" && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        throw new IllegalArgumentException("Invalid authorization header format");
    }

    @PostMapping("/refresh")
    public ResponseEntity<MasterResponseBody<String>> getFreshToken(@RequestHeader("Authorization") String token) throws Exception {

        String myRefreshToken = extractToken(token);
        System.out.println("My refresh token is: " + myRefreshToken);

//        String username = jwtUtil.extractUsername(myRefreshToken);
//        String freshToken = jwtUtil.generateToken(username);

        GetUserDetailsAfterAuthentication refreshedToken = userService.getFreshToken(myRefreshToken);
        return new ResponseEntity<>(refreshedToken,HttpStatusCode.valueOf(200));
    }

    @GetMapping("/token_info")
    public ResponseEntity<MasterResponseBody<String>> tokenInfo(@RequestHeader("Authorization") String authorizationHeader) {
        String token = extractToken(authorizationHeader);

       TokenInfo response = userService.tokenInfo(token);
        HttpStatus status = HttpStatus.valueOf(response.getStatus());
        return new ResponseEntity<>(response, status);
    }

    @PostMapping("/logout")
    public ResponseEntity<MasterResponseBody<String>> logoutUser(@RequestHeader("Authorization") String authorizationHeader) {
        String token = extractToken(authorizationHeader);
        blackList.blackListToken(token);
        return new ResponseEntity<>(new MasterResponseBody<>("Logged out successfully", 200), HttpStatus.OK);
    }
}




