package com.example.coursems.dto.request;

import lombok.Data;

@Data
public class LessonRequest {
    private String title;
    private String contentUrl;
    private String textContent;
    private int orderIndex;
}
