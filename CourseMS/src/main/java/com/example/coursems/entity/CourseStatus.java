package com.example.coursems.entity;

public enum CourseStatus {
    DRAFT,      // Bản nháp, chỉ teacher/admin thấy
    PUBLISHED,  // Đã công khai cho học viên đăng ký
    ARCHIVED    // Đã lưu trữ, không nhận học viên mới
}
