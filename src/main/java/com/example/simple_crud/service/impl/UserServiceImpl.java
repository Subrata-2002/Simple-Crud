package com.example.simple_crud.service.impl;

import com.example.simple_crud.controller.GetUserDetailsAfterAuthentication;
import com.example.simple_crud.controller.LoginResponse;
import com.example.simple_crud.controller.MasterResponseBody;
import com.example.simple_crud.controller.TokenInfo;
import com.example.simple_crud.dto.UserDto;
import com.example.simple_crud.entity.User;
import com.example.simple_crud.exception.IllegalArgumentException;
import com.example.simple_crud.exception.ResourceNotFoundException;
import com.example.simple_crud.repository.UserRepository;
import com.example.simple_crud.service.UserServices;
import com.example.simple_crud.utils.JwtUtil;
import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserServices {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private UserDto convertToUserDTO(User user) {
        UserDto UserDto = new UserDto();

        UserDto.setUsername(user.getUsername());
        UserDto.setEmail(user.getEmail());
        return UserDto;
    }

    @Override
    public MasterResponseBody<User> createUser(User user) {
//       private  bCryptPasswordEncoder = new Bcrypt
        try {
//            if (user.getUsername() == "" || !user.getUsername().matches("^[a-zA-Z][a-zA-Z0-9]{5,29}$")) {
//                throw new IllegalArgumentException("Username should start with an alphabet and contain 6-30 alphanumeric characters");
//            }
//            String UserName = user.getUsername();
//            String emailId = user.getEmail();
//            String Password = user.getPwd();
//
//           if(UserName.isEmpty() || emailId.isEmpty() || Password.isEmpty()){
//                        return new MasterResponseBody<User>("Username,emailId and Password can't be empty", 400);
//                    }

            String password = user.getPwd().trim();

            // Check if the password is empty
            if (password.isEmpty()) {
                return new MasterResponseBody<User>("Password can't be empty", 400);
            }

            // Check minimum length
            if (password.length() < 8) {
                return new MasterResponseBody<User>("Password must be at least 8 characters long", 400);
            }

            // Check for uppercase letter
            if (!Pattern.compile("[A-Z]").matcher(password).find()) {
                return new MasterResponseBody<User>("Password must contain at least one uppercase letter", 400);
            }

            // Check for lowercase letter
            if (!Pattern.compile("[a-z]").matcher(password).find()) {
                return new MasterResponseBody<User>("Password must contain at least one lowercase letter", 400);
            }

            // Check for digit
            if (!Pattern.compile("[0-9]").matcher(password).find()) {
                return new MasterResponseBody<User>("Password must contain at least one digit", 400);
            }

            // Check for special character
            if (!Pattern.compile("[!@#$%^&*()_+\\-=\\[\\]{};':\"\\|,.<>/?]").matcher(password).find()) {
                return new MasterResponseBody<User>("Password must contain at least one special character", 400);
            }
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encodedPassword = passwordEncoder.encode(user.getPwd());

            user.setPwd(encodedPassword);
            userRepository.save(user);
            return new MasterResponseBody<User>("User created successfully", 200);

        }catch (ConstraintViolationException e) {
            // Handle validation errors
            String errorMessage = e.getConstraintViolations().stream()
                    .findFirst()
                    .map(violation -> violation.getMessage())
                    .orElse("Validation failed ");
        return new MasterResponseBody<User>("Validation failed : " + errorMessage, 400);

        }catch (DataIntegrityViolationException e) {
            String errorMessage = e.getMessage().split(":")[1].trim();
            return new MasterResponseBody<User>("Database constraint violation: " + errorMessage, 400);

        }catch (ResourceNotFoundException e) {
            return new MasterResponseBody<>("User not found", 404);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return new MasterResponseBody<User>("An unexpected error occurred!!"+e.getMessage(),501);
        }
    }

    @Override
    public LoginResponse loginUser(String logindata, String pwd) {
        try {
            Optional<User> userOptional;

            if (logindata.contains("@")) {
                userOptional = userRepository.findByEmail(logindata.toLowerCase());
            } else {
                userOptional = userRepository.findByUsername(logindata);
            }
            // If user is not found, return appropriate response
            if (!userOptional.isPresent()) {

                return new LoginResponse("User not found", 404);
            }
            User user = userOptional.get();

            // Check if the provided password matches the stored password
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

            if (passwordEncoder.matches(pwd, user.getPwd())) {
                String jwtToken = jwtUtil.generateToken(user.getUsername());
                String jwtRefreshToken = jwtUtil.generateRefreshToken(user.getUsername());

                System.out.println("here the user.getUsername() data is : " + user.getUsername());
                System.out.println("Jwt Token is: " + jwtToken);
                System.out.println("Jwt Refresh Token is: " + jwtRefreshToken);

                return new LoginResponse(jwtToken, jwtRefreshToken, "Login successful", 200);

            } else {
                return new LoginResponse("Invalid password", 401);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new LoginResponse("An unexpected error occurred: " + e.getMessage(), 501);
        }
    }


    @Override
    public GetUserDetailsAfterAuthentication getUserDetails(String token) {
        try {
            if (jwtUtil.isTokenExpired(token)) {
                System.out.println("Token expired");
                return new GetUserDetailsAfterAuthentication("Token expired please login again", 401);
            }
            String tokenType = jwtUtil.getTokenType(token);
            if (!"access".equals(tokenType)) {
                return new GetUserDetailsAfterAuthentication("Access token required", 403);
            }

            String username2 = jwtUtil.extractUsername(token);
            if (!jwtUtil.validateToken(token, username2)) {
                return new GetUserDetailsAfterAuthentication("Invalid access token", 403);
            }

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                String username = userDetails.getUsername();

                Optional<User> userOptional = userRepository.findByUsername(username);
                if (userOptional.isPresent()) {
                    User user = userOptional.get();
                    UserDto userDto = convertToUserDTO(user);
                    return new GetUserDetailsAfterAuthentication(userDto, "User details fetched successfully", 200);
                } else {
                    return new GetUserDetailsAfterAuthentication("User not found", 404);
                }
            } else {
                return new GetUserDetailsAfterAuthentication("User not authenticated", 401);
            }
        } catch (Exception e) {
            return new GetUserDetailsAfterAuthentication("An unexpected error occurred: " + e.getMessage(), 501);
        }
    }

    @Override
    public GetUserDetailsAfterAuthentication getFreshToken(String refreshToken) {
//
        try {
            System.out.println("Validating token: " + refreshToken);
            System.out.println("Extracted username: " + jwtUtil.extractUsername(refreshToken));

            String tokenType = jwtUtil.getTokenType(refreshToken);
            if (!"refresh".equals(tokenType)) {
                return new GetUserDetailsAfterAuthentication("Refresh token required", 403);
            }
            if (!jwtUtil.validateToken(refreshToken, jwtUtil.extractUsername(refreshToken))) {
                String username = jwtUtil.extractUsername(refreshToken);
                String freshToken = jwtUtil.generateToken(username);
                System.out.println("fresh Token is: " + freshToken);
                return new GetUserDetailsAfterAuthentication(freshToken, "Fresh JWT token generated successfully", 200);
            } else {
                System.out.println("Invalid refresh token");
                return new GetUserDetailsAfterAuthentication("Invalid token", 401);
            }
        } catch (Exception e) {
            return new GetUserDetailsAfterAuthentication("An unexpected error occurred: " + e.getMessage(), 501);
        }
    }


    @Override
    public TokenInfo tokenInfo(String token) {
        try {
            String tokenType = jwtUtil.getTokenType(token);
            System.out.println("Tokentype is : " + tokenType);

            if (!"access".equals(tokenType)) {
                return new TokenInfo("Access token required", 403);
            }
            System.out.println("expi " + jwtUtil.isTokenExpired(token));

            if (jwtUtil.isTokenExpired(token)) {
                return new TokenInfo("Token expired please login again", 401);
            }

            if (!jwtUtil.validateToken(token, jwtUtil.extractUsername(token))) {
                return new TokenInfo("Invalid access token", 403);
            }

            String tokenInfo = jwtUtil.extractUsername(token);
            System.out.println("Token info: " + tokenInfo);
            return new TokenInfo(tokenInfo,"Details generated successfully" ,200);
        } catch (Exception e) {
            return new TokenInfo("An unexpected error occurred: " + e.getMessage(), 501);
        }
    }

    @Override
    public MasterResponseBody<String> logoutUser(String token) {
        try {
            if (jwtUtil.validateToken(token, jwtUtil.extractUsername(token))) {
                System.out.println("Validating token: " + token);
                System.out.println("Extracted username: " + jwtUtil.extractUsername(token));

                return new MasterResponseBody<>("Logged out successfully", 200);
            } else {
                System.out.println("Invalid token");
                return new MasterResponseBody<>("Invalid token", 401);
            }
        } catch (Exception e) {
            return new MasterResponseBody<>("An unexpected error occurred: " + e.getMessage(), 501);
        }
    }


}
