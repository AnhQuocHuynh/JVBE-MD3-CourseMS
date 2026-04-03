package com.example.coursems.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ReviewRequest {
    @NotNull(message = "Diem danh gia khong duoc de trong")
    @Min(value = 1, message = "Diem danh gia toi thieu la 1")
    @Max(value = 5, message = "Diem danh gia toi da la 5")
    private Integer rating;

    @Size(max = 1000, message = "Noi dung danh gia khong duoc qua 1000 ky tu")
    private String comment;
}
