package com.example.coursems.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CourseRequest {
    
    @NotBlank(message = "Tiêu đề khóa học không được để trống")
    private String title;
    
    private String description;
    
    @NotNull(message = "ID giảng viên không được để trống")
    private Integer teacherId; // Đổi sang Integer để check NotNull
    
    @NotNull(message = "Giá khóa học không được để trống")
    @Min(value = 0, message = "Giá khóa học không được âm")
    private BigDecimal price;
    
    @Min(value = 1, message = "Thời lượng khóa học ít nhất phải là 1 giờ")
    private int durationHours;
}
