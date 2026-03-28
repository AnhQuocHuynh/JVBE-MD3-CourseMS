package com.example.coursems.dto.request;

import com.example.coursems.entity.CourseStatus;
import lombok.Data;

@Data
public class CourseStatusRequest {
    private CourseStatus status;
}
