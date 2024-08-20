package com.example.simple_crud.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="users")

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="user_name", nullable = false, unique = true)
    @NotEmpty(message = "The username is required.")
    private String username;

    @Column(name="email_id", nullable = false, unique = true)
    @NotEmpty(message = "The email is required.")
    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}", message = "Please give a valid email id.")
    private String email;

    @Column(name="password", nullable = false)
    @NotEmpty(message = "Password is required.")
    private String pwd;

    @Column(name="country")
    private String country;

    @Column(name="status", nullable = false)
    private Boolean status = true ;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="created_at")
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="updated_at")
    private Date updatedAt;

    @PrePersist
    protected void onCreate(){
        Date now = new Date();
        createdAt = now;
        updatedAt= now;
    }

    @PreUpdate
    protected void onUpdate(){
        updatedAt = new Date();
    }
    public void setEmail(String email){
        this.email = email.toLowerCase();
    }
    public void setUsername(String username){
        String name = "^[A-Za-z]\\w{5,29}$";
        System.out.println("here you can check");
       if(username.matches(name)){
           this.username = username;
//
       }
//        throw new IllegalArgumentException("Username should start with alphabet and contain 6-30 alphanumeric characters");
    }
}
