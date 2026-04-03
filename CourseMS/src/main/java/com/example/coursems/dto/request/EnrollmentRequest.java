package com.example.coursems.dto.request;

import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class EnrollmentRequest {
    @Positive(message = "ID khoa hoc phai lon hon 0")
    private int courseId;
}
