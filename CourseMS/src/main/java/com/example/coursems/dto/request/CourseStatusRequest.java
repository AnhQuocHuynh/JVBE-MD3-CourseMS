package com.example.coursems.dto.request;

import com.example.coursems.entity.CourseStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CourseStatusRequest {
    @NotNull(message = "Trang thai khoa hoc khong duoc de trong")
    private CourseStatus status;
}
