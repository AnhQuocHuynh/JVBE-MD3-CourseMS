package com.example.coursems.service.impl;

import com.example.coursems.dto.response.TopCourseReportResponse;
import com.example.coursems.entity.Course;
import com.example.coursems.repository.CourseRepository;
import com.example.coursems.repository.EnrollmentRepository;
import com.example.coursems.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;

    @Override
    @Transactional(readOnly = true)
    public List<TopCourseReportResponse> getTopCourses(int limit) {
        int safeLimit = Math.max(limit, 1);
        return courseRepository.findAll().stream()
                .map(this::toReport)
                .sorted(Comparator.comparingLong(TopCourseReportResponse::getEnrollmentCount).reversed())
                .limit(safeLimit)
                .toList();
    }

    private TopCourseReportResponse toReport(Course course) {
        long count = enrollmentRepository.findByCourse_CourseId(course.getCourseId()).size();
        return TopCourseReportResponse.builder()
                .courseId(course.getCourseId())
                .title(course.getTitle())
                .teacherId(course.getTeacher().getUserId())
                .teacherName(course.getTeacher().getFullName())
                .enrollmentCount(count)
                .build();
    }
}
