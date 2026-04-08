package com.example.coursems.dto.request;

import com.example.coursems.entity.NotificationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NotificationCreateRequest {
    @NotNull(message = "ID nguoi dung khong duoc de trong")
    @Positive(message = "ID nguoi dung phai lon hon 0")
    private Integer userId;

    @NotNull(message = "Loai thong bao khong duoc de trong")
    private NotificationType type;

    @NotBlank(message = "Noi dung thong bao khong duoc de trong")
    @Size(max = 2000, message = "Noi dung thong bao khong duoc qua 2000 ky tu")
    private String message;
}
