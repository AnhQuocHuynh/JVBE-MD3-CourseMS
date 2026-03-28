package com.example.coursems.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CourseRequest {
    private String title;
    private String description;
    private int teacherId;
    private BigDecimal price;
    private int durationHours;
}
