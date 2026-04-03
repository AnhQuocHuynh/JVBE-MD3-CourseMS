package com.example.coursems.util;

import com.example.coursems.entity.Role;
import com.example.coursems.entity.User;
import com.example.coursems.repository.CourseRepository;
import com.example.coursems.repository.EnrollmentRepository;
import com.example.coursems.repository.LessonRepository;
import com.example.coursems.repository.ReviewRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("securityUtil")
public class SecurityUtil {
    private final CourseRepository courseRepository;
    private final LessonRepository lessonRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final ReviewRepository reviewRepository;

    public SecurityUtil(
            CourseRepository courseRepository,
            LessonRepository lessonRepository,
            EnrollmentRepository enrollmentRepository,
            ReviewRepository reviewRepository
    ) {
        this.courseRepository = courseRepository;
        this.lessonRepository = lessonRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.reviewRepository = reviewRepository;
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof User user)) {
            return null;
        }
        return user;
    }

    public int getCurrentUserId() {
        User user = getCurrentUser();
        return user == null ? -1 : user.getUserId();
    }

    public boolean isAdmin() {
        User user = getCurrentUser();
        return user != null && user.getRole() == Role.ADMIN;
    }

    public boolean isOwnerOrAdmin(int userId) {
        User user = getCurrentUser();
        return user != null && (user.getRole() == Role.ADMIN || user.getUserId() == userId);
    }

    public boolean isTeacherOfCourseOrAdmin(int courseId) {
        User user = getCurrentUser();
        if (user == null) {
            return false;
        }
        if (user.getRole() == Role.ADMIN) {
            return true;
        }
        if (user.getRole() != Role.TEACHER) {
            return false;
        }
        return courseRepository.findById(courseId)
                .map(course -> course.getTeacher().getUserId() == user.getUserId())
                .orElse(false);
    }

    public boolean isTeacherOfLessonOrAdmin(int lessonId) {
        User user = getCurrentUser();
        if (user == null) {
            return false;
        }
        if (user.getRole() == Role.ADMIN) {
            return true;
        }
        if (user.getRole() != Role.TEACHER) {
            return false;
        }
        return lessonRepository.findById(lessonId)
                .map(lesson -> lesson.getCourse().getTeacher().getUserId() == user.getUserId())
                .orElse(false);
    }

    public boolean isEnrollmentOwnerOrAdmin(int enrollmentId) {
        User user = getCurrentUser();
        if (user == null) {
            return false;
        }
        if (user.getRole() == Role.ADMIN) {
            return true;
        }
        return enrollmentRepository.findById(enrollmentId)
                .map(enrollment -> enrollment.getStudent().getUserId() == user.getUserId())
                .orElse(false);
    }

    public boolean isReviewOwnerOrAdmin(int reviewId) {
        User user = getCurrentUser();
        if (user == null) {
            return false;
        }
        if (user.getRole() == Role.ADMIN) {
            return true;
        }
        return reviewRepository.findById(reviewId)
                .map(review -> review.getStudent().getUserId() == user.getUserId())
                .orElse(false);
    }
}
