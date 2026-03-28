package com.example.coursems.dto.request;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private String email;
    private String fullName;
}
