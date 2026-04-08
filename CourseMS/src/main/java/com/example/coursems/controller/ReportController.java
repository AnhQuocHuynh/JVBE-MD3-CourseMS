package com.example.coursems.controller;

import com.example.coursems.dto.response.ApiResponse;
import com.example.coursems.dto.response.StudentProgressReportResponse;
import com.example.coursems.dto.response.TeacherCoursesOverviewResponse;
import com.example.coursems.dto.response.TopCourseReportResponse;
import com.example.coursems.service.ReportService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@Validated
public class ReportController {
    private final ReportService reportService;

    @GetMapping("/top_courses")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<TopCourseReportResponse>> getTopCourses(
            @RequestParam(defaultValue = "5") @Positive int limit) {
        return ApiResponse.success("Lay bao cao top khoa hoc thanh cong", reportService.getTopCourses(limit));
    }

    @GetMapping("/student_progress/{student_id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<StudentProgressReportResponse> getStudentProgress(
            @PathVariable("student_id") @Positive int studentId) {
        return ApiResponse.success("Lay bao cao tien do hoc sinh vien thanh cong", reportService.getStudentProgress(studentId));
    }

    @GetMapping("/teacher_courses_overview/{teacher_id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<TeacherCoursesOverviewResponse> getTeacherCoursesOverview(
            @PathVariable("teacher_id") @Positive int teacherId) {
        return ApiResponse.success("Lay bao cao tong quan khoa hoc giang vien thanh cong", reportService.getTeacherCoursesOverview(teacherId));
    }
}
