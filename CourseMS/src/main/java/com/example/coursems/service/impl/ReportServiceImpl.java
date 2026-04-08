package com.example.coursems.service.impl;

import com.example.coursems.config.exception.ResourceNotFoundException;
import com.example.coursems.dto.response.StudentProgressReportResponse;
import com.example.coursems.dto.response.TeacherCoursesOverviewResponse;
import com.example.coursems.dto.response.TopCourseReportResponse;
import com.example.coursems.entity.Course;
import com.example.coursems.entity.CourseStatus;
import com.example.coursems.entity.Enrollment;
import com.example.coursems.entity.EnrollmentStatus;
import com.example.coursems.entity.Role;
import com.example.coursems.entity.User;
import com.example.coursems.repository.CourseRepository;
import com.example.coursems.repository.EnrollmentRepository;
import com.example.coursems.repository.UserRepository;
import com.example.coursems.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;

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

    @Override
    @Transactional(readOnly = true)
    public StudentProgressReportResponse getStudentProgress(int studentId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Khong tim thay nguoi dung voi ID: " + studentId));
        if (student.getRole() != Role.STUDENT) {
            throw new ResourceNotFoundException("Nguoi dung voi ID " + studentId + " khong phai STUDENT.");
        }

        List<Enrollment> enrollments = enrollmentRepository.findByStudent_UserId(studentId);
        long totalEnrollments = enrollments.size();
        long completedCourses = enrollments.stream()
                .filter(e -> e.getStatus() == EnrollmentStatus.COMPLETED)
                .count();

        BigDecimal averageProgress = totalEnrollments == 0
                ? BigDecimal.ZERO
                : enrollments.stream()
                .map(Enrollment::getProgressPercentage)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(totalEnrollments), 2, RoundingMode.HALF_UP);

        return StudentProgressReportResponse.builder()
                .studentId(studentId)
                .studentName(student.getFullName())
                .totalEnrollments(totalEnrollments)
                .completedCourses(completedCourses)
                .averageProgressPercentage(averageProgress)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public TeacherCoursesOverviewResponse getTeacherCoursesOverview(int teacherId) {
        User teacher = userRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Khong tim thay nguoi dung voi ID: " + teacherId));
        if (teacher.getRole() != Role.TEACHER) {
            throw new ResourceNotFoundException("Nguoi dung voi ID " + teacherId + " khong phai TEACHER.");
        }

        List<Course> courses = courseRepository.findByTeacher_UserId(teacherId);
        long totalCourses = courses.size();
        long draftCourses = courses.stream().filter(c -> c.getStatus() == CourseStatus.DRAFT).count();
        long publishedCourses = courses.stream().filter(c -> c.getStatus() == CourseStatus.PUBLISHED).count();
        long archivedCourses = courses.stream().filter(c -> c.getStatus() == CourseStatus.ARCHIVED).count();
        long totalEnrollments = courses.stream()
                .mapToLong(c -> enrollmentRepository.findByCourse_CourseId(c.getCourseId()).size())
                .sum();

        return TeacherCoursesOverviewResponse.builder()
                .teacherId(teacherId)
                .teacherName(teacher.getFullName())
                .totalCourses(totalCourses)
                .draftCourses(draftCourses)
                .publishedCourses(publishedCourses)
                .archivedCourses(archivedCourses)
                .totalEnrollments(totalEnrollments)
                .build();
    }
}
