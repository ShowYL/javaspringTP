package com.example.db;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;

@Entity
public class User {

    @Id
    private String username;

    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    public User(String username, String password, UserRole role){
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public User() {}

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }

    public UserRole getRole(){
        return role;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(UserRole role){
        this.role = role;
    }
}
