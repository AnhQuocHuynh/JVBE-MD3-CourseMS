package com.example.coursems.service;

import com.example.coursems.dto.response.TopCourseReportResponse;

import java.util.List;

public interface ReportService {
    List<TopCourseReportResponse> getTopCourses(int limit);
}
