package com.example.coursems.controller;

import com.example.coursems.dto.response.ApiResponse;
import com.example.coursems.dto.response.NotificationResponse;
import com.example.coursems.service.NotificationService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
}
