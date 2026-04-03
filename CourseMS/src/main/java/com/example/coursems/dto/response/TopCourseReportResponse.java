package com.example.coursems.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopCourseReportResponse {
    private int courseId;
    private String title;
    private int teacherId;
    private String teacherName;
    private long enrollmentCount;
}
