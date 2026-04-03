package com.example.coursems.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserPasswordRequest {
    @NotBlank(message = "Mat khau hien tai khong duoc de trong")
    private String currentPassword;

    @NotBlank(message = "Mat khau moi khong duoc de trong")
    @Size(min = 6, max = 100, message = "Mat khau moi phai tu 6 den 100 ky tu")
    private String newPassword;
}
