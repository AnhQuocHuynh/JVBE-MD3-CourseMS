package com.example.coursems.dto.request;

import lombok.Data;

@Data
public class UserPasswordRequest {
    private String currentPassword;
    private String newPassword;
}
