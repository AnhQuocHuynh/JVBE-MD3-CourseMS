package com.example.coursems.dto.request;

import com.example.coursems.entity.Role;
import lombok.Data;

@Data
public class UserCreateRequest {
    private String username;
    private String password;
    private String email;
    private String fullName;
    private Role role;
}
