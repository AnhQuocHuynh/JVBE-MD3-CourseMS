package com.example.coursems.service;

import com.example.coursems.dto.response.StudentProgressReportResponse;
import com.example.coursems.dto.response.TeacherCoursesOverviewResponse;
import com.example.coursems.dto.response.TopCourseReportResponse;

import java.util.List;

public interface ReportService {
    List<TopCourseReportResponse> getTopCourses(int limit);
    StudentProgressReportResponse getStudentProgress(int studentId);
    TeacherCoursesOverviewResponse getTeacherCoursesOverview(int teacherId);
}
