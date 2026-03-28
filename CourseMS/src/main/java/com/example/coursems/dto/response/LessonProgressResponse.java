package com.example.coursems.dto.response;

import lombok.Data;

import java.util.Date;

@Data
public class LessonProgressResponse {
    private int progressId;
    private int lessonId;
    private String lessonTitle;
    private boolean isCompleted;
    private Date completedAt;
    private Date lastAccessedAt;
}
