package com.example.coursems.controller;

import com.example.coursems.dto.response.ApiResponse;
import com.example.coursems.dto.response.TopCourseReportResponse;
import com.example.coursems.service.ReportService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
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
}
