package com.example.coursems.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherCoursesOverviewResponse {
    private int teacherId;
    private String teacherName;
    private long totalCourses;
    private long draftCourses;
    private long publishedCourses;
    private long archivedCourses;
    private long totalEnrollments;
}
