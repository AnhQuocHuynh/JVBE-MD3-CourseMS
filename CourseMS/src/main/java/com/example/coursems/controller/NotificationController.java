package com.example.coursems.controller;

import com.example.coursems.dto.request.NotificationCreateRequest;
import com.example.coursems.dto.response.ApiResponse;
import com.example.coursems.dto.response.NotificationResponse;
import com.example.coursems.service.NotificationService;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Validated
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping
    public ApiResponse<List<NotificationResponse>> getNotifications() {
        return ApiResponse.success("Lay danh sach thong bao thanh cong", notificationService.getMyNotifications());
    }

    @PutMapping("/{id}/read")
    public ApiResponse<NotificationResponse> markAsRead(@PathVariable("id") @Positive int id) {
        return ApiResponse.success("Danh dau thong bao da doc thanh cong", notificationService.markAsRead(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<NotificationResponse> createNotification(@Valid @RequestBody NotificationCreateRequest request) {
        return ApiResponse.success("Tao thong bao thanh cong", notificationService.createNotification(request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> deleteNotification(@PathVariable("id") @Positive int id) {
        notificationService.deleteNotification(id);
        return ApiResponse.success("Xoa thong bao thanh cong", null);
    }
}
