package com.example.coursems.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentProgressReportResponse {
    private int studentId;
    private String studentName;
    private long totalEnrollments;
    private long completedCourses;
    private BigDecimal averageProgressPercentage;
}
