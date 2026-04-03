package com.example.coursems.service;

import com.example.coursems.dto.request.AuthRequest;
import com.example.coursems.dto.response.AuthResponse;
import com.example.coursems.dto.request.RegisterRequest;
import com.example.coursems.dto.response.UserResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse authenticate(AuthRequest request);
    boolean verifyToken();
    boolean logout(String authHeader);
    UserResponse getCurrentUser();
}
