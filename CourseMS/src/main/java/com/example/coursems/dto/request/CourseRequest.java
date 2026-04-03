package com.example.coursems.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CourseRequest {
    @NotBlank(message = "Tieu de khoa hoc khong duoc de trong")
    @Size(max = 255, message = "Tieu de khoa hoc khong duoc qua 255 ky tu")
    private String title;

    @Size(max = 2000, message = "Mo ta khong duoc qua 2000 ky tu")
    private String description;

    @NotNull(message = "ID giang vien khong duoc de trong")
    @Positive(message = "ID giang vien phai lon hon 0")
    private Integer teacherId;

    @NotNull(message = "Gia khoa hoc khong duoc de trong")
    @DecimalMin(value = "0.0", inclusive = true, message = "Gia khoa hoc khong duoc am")
    private BigDecimal price;

    @Positive(message = "Thoi luong khoa hoc phai lon hon 0")
    private int durationHours;
}
