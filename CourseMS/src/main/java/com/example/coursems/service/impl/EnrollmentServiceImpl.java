package com.example.coursems.service.impl;

import com.example.coursems.config.exception.BadRequestException;
import com.example.coursems.config.exception.DuplicateResourceException;
import com.example.coursems.config.exception.ForbiddenException;
import com.example.coursems.config.exception.ResourceNotFoundException;
import com.example.coursems.dto.request.EnrollmentRequest;
import com.example.coursems.dto.response.EnrollmentResponse;
import com.example.coursems.entity.Course;
import com.example.coursems.entity.CourseStatus;
import com.example.coursems.entity.Enrollment;
import com.example.coursems.entity.EnrollmentStatus;
import com.example.coursems.entity.Lesson;
import com.example.coursems.entity.LessonProgress;
import com.example.coursems.entity.Role;
import com.example.coursems.entity.User;
import com.example.coursems.mapper.EnrollmentMapper;
import com.example.coursems.repository.CourseRepository;
import com.example.coursems.repository.EnrollmentRepository;
import com.example.coursems.repository.LessonProgressRepository;
import com.example.coursems.repository.LessonRepository;
import com.example.coursems.service.EnrollmentService;
import com.example.coursems.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final LessonRepository lessonRepository;
    private final LessonProgressRepository lessonProgressRepository;
    private final EnrollmentMapper enrollmentMapper;
    private final SecurityUtil securityUtil;

    @Override
    @Transactional(readOnly = true)
    public List<EnrollmentResponse> getMyEnrollments() {
        User user = requireCurrentUser();
        if (user.getRole() != Role.STUDENT) {
            throw new ForbiddenException("Chi hoc vien moi duoc xem danh sach ghi danh cua minh.");
        }
        return enrollmentRepository.findByStudent_UserId(user.getUserId()).stream()
                .map(enrollmentMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public EnrollmentResponse getEnrollmentById(int enrollmentId) {
        Enrollment enrollment = getEnrollmentOrThrow(enrollmentId);
        if (!securityUtil.isEnrollmentOwnerOrAdmin(enrollmentId)) {
            throw new ForbiddenException("Ban khong duoc phep xem ghi danh nay.");
        }
        return enrollmentMapper.toResponse(enrollment);
    }

    @Override
    @Transactional
    public EnrollmentResponse enroll(EnrollmentRequest request) {
        User user = requireCurrentUser();
        if (user.getRole() != Role.STUDENT) {
            throw new ForbiddenException("Chi hoc vien moi duoc dang ky khoa hoc.");
        }

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Khong tim thay khoa hoc voi ID: " + request.getCourseId()));
        if (course.getStatus() != CourseStatus.PUBLISHED) {
            throw new BadRequestException("Chi duoc dang ky khoa hoc o trang thai PUBLISHED.");
        }
        if (enrollmentRepository.existsByStudent_UserIdAndCourse_CourseId(user.getUserId(), request.getCourseId())) {
            throw new DuplicateResourceException("Ban da dang ky khoa hoc nay.");
        }

        Enrollment enrollment = Enrollment.builder()
                .student(user)
                .course(course)
                .status(EnrollmentStatus.ENROLLED)
                .progressPercentage(BigDecimal.ZERO)
                .build();
        Enrollment saved = enrollmentRepository.save(enrollment);
        return enrollmentMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public EnrollmentResponse completeLesson(int enrollmentId, int lessonId) {
        Enrollment enrollment = getEnrollmentOrThrow(enrollmentId);
        if (!securityUtil.isEnrollmentOwnerOrAdmin(enrollmentId)) {
            throw new ForbiddenException("Ban khong duoc phep cap nhat tien do ghi danh nay.");
        }
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Khong tim thay bai hoc voi ID: " + lessonId));
        if (lesson.getCourse().getCourseId() != enrollment.getCourse().getCourseId()) {
            throw new BadRequestException("Bai hoc khong thuoc khoa hoc cua ghi danh nay.");
        }

        LessonProgress progress = lessonProgressRepository
                .findByEnrollment_EnrollmentIdAndLesson_LessonId(enrollmentId, lessonId)
                .orElseGet(() -> LessonProgress.builder()
                        .enrollment(enrollment)
                        .lesson(lesson)
                        .isCompleted(false)
                        .build());

        progress.setCompleted(true);
        progress.setCompletedAt(new Date());
        progress.setLastAccessedAt(new Date());
        lessonProgressRepository.save(progress);

        long totalLessons = lessonRepository.findByCourse_CourseIdOrderByOrderIndexAsc(enrollment.getCourse().getCourseId()).size();
        long completedLessons = lessonProgressRepository.countByEnrollment_EnrollmentIdAndIsCompletedTrue(enrollmentId);
        if (totalLessons > 0) {
            BigDecimal progressPercent = BigDecimal.valueOf(completedLessons * 100.0 / totalLessons)
                    .setScale(2, RoundingMode.HALF_UP);
            enrollment.setProgressPercentage(progressPercent);
            if (completedLessons == totalLessons) {
                enrollment.setStatus(EnrollmentStatus.COMPLETED);
            }
        }
        Enrollment saved = enrollmentRepository.save(enrollment);
        return enrollmentMapper.toResponse(saved);
    }

    private Enrollment getEnrollmentOrThrow(int enrollmentId) {
        return enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Khong tim thay ghi danh voi ID: " + enrollmentId));
    }

    private User requireCurrentUser() {
        User user = securityUtil.getCurrentUser();
        if (user == null) {
            throw new ForbiddenException("Ban chua dang nhap.");
        }
        return user;
    }
}
