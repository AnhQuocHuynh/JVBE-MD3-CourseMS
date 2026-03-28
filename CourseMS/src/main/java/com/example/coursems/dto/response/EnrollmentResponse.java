package com.example.coursems.dto.response;

import com.example.coursems.entity.EnrollmentStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class EnrollmentResponse {
    private int enrollmentId;
    private int studentId;
    private String studentName;
    private int courseId;
    private String courseTitle;
    private Date enrollmentDate;
    private EnrollmentStatus status;
    private BigDecimal progressPercentage;
    private List<LessonProgressResponse> lessonProgressList;
}
