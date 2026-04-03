package com.example.coursems.service;

import com.example.coursems.dto.request.ReviewRequest;
import com.example.coursems.dto.response.ReviewResponse;

import java.util.List;

public interface ReviewService {
    List<ReviewResponse> getReviewsByCourse(int courseId);
    ReviewResponse createReview(int courseId, ReviewRequest request);
    ReviewResponse updateReview(int reviewId, ReviewRequest request);
    void deleteReview(int reviewId);
}
