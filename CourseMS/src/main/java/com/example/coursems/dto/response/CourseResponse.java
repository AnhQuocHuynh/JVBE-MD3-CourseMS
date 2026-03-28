package com.example.coursems.dto.response;

import com.example.coursems.entity.CourseStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class CourseResponse {
    private int courseId;
    private String title;
    private String description;
    private int teacherId;
    private String teacherName;
    private BigDecimal price;
    private int durationHours;
    private CourseStatus status;
    private Date createdAt;
    private Date updatedAt;
    private List<LessonResponse> lessons;
}
