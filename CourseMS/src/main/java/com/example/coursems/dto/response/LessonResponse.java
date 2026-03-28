package com.example.coursems.dto.response;

import lombok.Data;

@Data
public class LessonResponse {
    private int lessonId;
    private int courseId;
    private String title;
    private String contentUrl;
    private String textContent;
    private int orderIndex;
    private boolean isPublished;
}
