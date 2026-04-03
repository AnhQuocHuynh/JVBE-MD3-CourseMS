package com.example.coursems.controller;

import com.example.coursems.dto.request.UserCreateRequest;
import com.example.coursems.dto.request.UserPasswordRequest;
import com.example.coursems.dto.request.UserRoleRequest;
import com.example.coursems.dto.request.UserStatusRequest;
import com.example.coursems.dto.request.UserUpdateRequest;
import com.example.coursems.dto.response.ApiResponse;
import com.example.coursems.dto.response.PaginatedData;
import com.example.coursems.dto.response.UserResponse;
import com.example.coursems.entity.Role;
import com.example.coursems.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<PaginatedData<UserResponse>> getUsers(
            @RequestParam(required = false) Role role,
            @RequestParam(required = false) Boolean status,
            @RequestParam(defaultValue = "1") @Positive int page,
            @RequestParam(defaultValue = "10") @Positive int size) {
        List<UserResponse> allItems = userService.getAllUsers(role, status);
        int fromIndex = Math.min((page - 1) * size, allItems.size());
        int toIndex = Math.min(fromIndex + size, allItems.size());
        List<UserResponse> items = fromIndex >= toIndex ? Collections.emptyList() : allItems.subList(fromIndex, toIndex);
        return ApiResponse.successPage("Lay danh sach nguoi dung thanh cong", items, page, size, allItems.size());
    }

    @GetMapping("/{user_id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<UserResponse> getUserById(@PathVariable("user_id") @Positive int userId) {
        return ApiResponse.success("Lay thong tin nguoi dung thanh cong", userService.getUserById(userId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<UserResponse> createUser(@Valid @RequestBody UserCreateRequest request) {
        return ApiResponse.success("Tao nguoi dung thanh cong", userService.createUser(request));
    }

    @PutMapping("/{user_id}")
    @PreAuthorize("@securityUtil.isOwnerOrAdmin(#userId)")
    public ApiResponse<UserResponse> updateUser(
            @PathVariable("user_id") @Positive int userId,
            @Valid @RequestBody UserUpdateRequest request) {
        return ApiResponse.success("Cap nhat nguoi dung thanh cong", userService.updateUser(userId, request));
    }

    @PutMapping("/{user_id}/password")
    @PreAuthorize("@securityUtil.isOwnerOrAdmin(#userId)")
    public ApiResponse<Void> changePassword(
            @PathVariable("user_id") @Positive int userId,
            @Valid @RequestBody UserPasswordRequest request) {
        userService.changePassword(userId, request);
        return ApiResponse.success("Doi mat khau thanh cong", null);
    }

    @PutMapping("/{user_id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<UserResponse> updateRole(
            @PathVariable("user_id") @Positive int userId,
            @Valid @RequestBody UserRoleRequest request) {
        return ApiResponse.success("Cap nhat role thanh cong", userService.updateRole(userId, request));
    }

    @PutMapping("/{user_id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<UserResponse> updateStatus(
            @PathVariable("user_id") @Positive int userId,
            @Valid @RequestBody UserStatusRequest request) {
        return ApiResponse.success("Cap nhat trang thai thanh cong", userService.updateStatus(userId, request));
    }

    @DeleteMapping("/{user_id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> deleteUser(@PathVariable("user_id") @Positive int userId) {
        userService.deleteUser(userId);
        return ApiResponse.success("Xoa nguoi dung thanh cong", null);
    }
}
