package com.example.coursems.dto.response;

import lombok.Data;

import java.util.Date;

@Data
public class ReviewResponse {
    private int reviewId;
    private int courseId;
    private int studentId;
    private String studentName;
    private int rating;
    private String comment;
    private Date createdAt;
}
