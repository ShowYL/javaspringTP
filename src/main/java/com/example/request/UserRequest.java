package com.example.request;

import com.example.db.UserRole;

public record UserRequest(String username, String password, UserRole role) {}