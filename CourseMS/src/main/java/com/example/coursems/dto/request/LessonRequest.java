package com.example.coursems.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LessonRequest {
    @NotBlank(message = "Tieu de bai hoc khong duoc de trong")
    @Size(max = 255, message = "Tieu de bai hoc khong duoc qua 255 ky tu")
    private String title;

    @Size(max = 500, message = "Duong dan noi dung khong duoc qua 500 ky tu")
    private String contentUrl;

    @Size(max = 20000, message = "Noi dung van ban khong duoc qua 20000 ky tu")
    private String textContent;

    @Positive(message = "Thu tu bai hoc phai lon hon 0")
    private int orderIndex;
}
