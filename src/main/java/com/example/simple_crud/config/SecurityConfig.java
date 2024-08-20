package com.example.simple_crud.config;

import com.example.simple_crud.filter.JwtRequestFilter;
import com.example.simple_crud.repository.UserRepository;
import jakarta.servlet.Filter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig  {

    private final JwtRequestFilter JwtRequestFilter;
    private final UserRepository userRepository;

    public SecurityConfig(JwtRequestFilter jwtRequestFilter, UserRepository userRepository) {
        this.JwtRequestFilter = jwtRequestFilter;
        this.userRepository = userRepository;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())  // Disable CSRF protection for simplicity
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/**").permitAll()// Allow access to public endpoints
//                                .requestMatchers("/register", "/login").permitAll()  // Allow registration and login endpoints
                        .anyRequest().authenticated()  // Require authentication for all other endpoints
                )
//                .httpBasic(Customizer.withDefaults());  // Use HTTP Basic authentication
        .addFilterBefore((Filter) JwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        System.out.println("SecurityFilterChain created");
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
