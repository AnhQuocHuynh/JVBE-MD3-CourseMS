package com.example.coursems.service;

import com.example.coursems.dto.request.UserCreateRequest;
import com.example.coursems.dto.request.UserPasswordRequest;
import com.example.coursems.dto.request.UserRoleRequest;
import com.example.coursems.dto.request.UserStatusRequest;
import com.example.coursems.dto.request.UserUpdateRequest;
import com.example.coursems.dto.response.UserResponse;
import com.example.coursems.entity.Role;

import java.util.List;

public interface UserService {
    List<UserResponse> getAllUsers(Role role, Boolean isActive);
    UserResponse getUserById(int userId);
    UserResponse createUser(UserCreateRequest request);
    UserResponse updateUser(int userId, UserUpdateRequest request);
    UserResponse updateRole(int userId, UserRoleRequest request);
    UserResponse updateStatus(int userId, UserStatusRequest request);
    void changePassword(int userId, UserPasswordRequest request);
    void deleteUser(int userId);
}
