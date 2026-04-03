package com.example.coursems.controller;

import com.example.coursems.dto.request.ReviewRequest;
import com.example.coursems.dto.response.ApiResponse;
import com.example.coursems.dto.response.ReviewResponse;
import com.example.coursems.service.ReviewService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping("/api/courses/{course_id}/reviews")
    public ApiResponse<List<ReviewResponse>> getReviewsByCourse(
            @PathVariable("course_id") @Positive int courseId) {
        return ApiResponse.success("Lay danh sach danh gia thanh cong", reviewService.getReviewsByCourse(courseId));
    }

    @PostMapping("/api/courses/{course_id}/reviews")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('STUDENT')")
    public ApiResponse<ReviewResponse> createReview(
            @PathVariable("course_id") @Positive int courseId,
            @Valid @RequestBody ReviewRequest request) {
        return ApiResponse.success("Tao danh gia thanh cong", reviewService.createReview(courseId, request));
    }

    @PutMapping("/api/reviews/{review_id}")
    @PreAuthorize("@securityUtil.isReviewOwnerOrAdmin(#reviewId)")
    public ApiResponse<ReviewResponse> updateReview(
            @PathVariable("review_id") @Positive int reviewId,
            @Valid @RequestBody ReviewRequest request) {
        return ApiResponse.success("Cap nhat danh gia thanh cong", reviewService.updateReview(reviewId, request));
    }

    @DeleteMapping("/api/reviews/{review_id}")
    @PreAuthorize("@securityUtil.isReviewOwnerOrAdmin(#reviewId)")
    public ApiResponse<Void> deleteReview(@PathVariable("review_id") @Positive int reviewId) {
        reviewService.deleteReview(reviewId);
        return ApiResponse.success("Xoa danh gia thanh cong", null);
    }
}
