package com.example.coursems.service.impl;

import com.example.coursems.config.exception.BadRequestException;
import com.example.coursems.config.exception.DuplicateResourceException;
import com.example.coursems.config.exception.ForbiddenException;
import com.example.coursems.config.exception.ResourceNotFoundException;
import com.example.coursems.dto.request.ReviewRequest;
import com.example.coursems.dto.response.ReviewResponse;
import com.example.coursems.entity.Course;
import com.example.coursems.entity.Review;
import com.example.coursems.entity.Role;
import com.example.coursems.entity.User;
import com.example.coursems.repository.CourseRepository;
import com.example.coursems.repository.EnrollmentRepository;
import com.example.coursems.repository.ReviewRepository;
import com.example.coursems.service.ReviewService;
import com.example.coursems.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final SecurityUtil securityUtil;

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviewsByCourse(int courseId) {
        return reviewRepository.findByCourse_CourseId(courseId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public ReviewResponse createReview(int courseId, ReviewRequest request) {
        User user = requireCurrentUser();
        if (user.getRole() != Role.STUDENT) {
            throw new ForbiddenException("Chi hoc vien moi duoc danh gia khoa hoc.");
        }
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Khong tim thay khoa hoc voi ID: " + courseId));
        if (!enrollmentRepository.existsByStudent_UserIdAndCourse_CourseId(user.getUserId(), courseId)) {
            throw new ForbiddenException("Ban chi co the danh gia khoa hoc da dang ky.");
        }
        if (reviewRepository.existsByCourse_CourseIdAndStudent_UserId(courseId, user.getUserId())) {
            throw new DuplicateResourceException("Ban da danh gia khoa hoc nay.");
        }
        Review review = Review.builder()
                .course(course)
                .student(user)
                .rating(request.getRating())
                .comment(request.getComment())
                .build();
        return toResponse(reviewRepository.save(review));
    }

    @Override
    @Transactional
    public ReviewResponse updateReview(int reviewId, ReviewRequest request) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Khong tim thay danh gia voi ID: " + reviewId));
        if (!securityUtil.isReviewOwnerOrAdmin(reviewId)) {
            throw new ForbiddenException("Ban khong duoc phep cap nhat danh gia nay.");
        }
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        return toResponse(reviewRepository.save(review));
    }

    @Override
    @Transactional
    public void deleteReview(int reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Khong tim thay danh gia voi ID: " + reviewId));
        if (!securityUtil.isReviewOwnerOrAdmin(reviewId)) {
            throw new ForbiddenException("Ban khong duoc phep xoa danh gia nay.");
        }
        reviewRepository.delete(review);
    }

    private ReviewResponse toResponse(Review review) {
        ReviewResponse response = new ReviewResponse();
        response.setReviewId(review.getReviewId());
        response.setCourseId(review.getCourse().getCourseId());
        response.setStudentId(review.getStudent().getUserId());
        response.setStudentName(review.getStudent().getFullName());
        response.setRating(review.getRating());
        response.setComment(review.getComment());
        response.setCreatedAt(review.getCreatedAt());
        return response;
    }

    private User requireCurrentUser() {
        User user = securityUtil.getCurrentUser();
        if (user == null) {
            throw new BadRequestException("Khong tim thay nguoi dung hien tai.");
        }
        return user;
    }
}
