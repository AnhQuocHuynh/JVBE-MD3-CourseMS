package com.example.coursems.dto.request;

import com.example.coursems.entity.Role;
import lombok.Data;

@Data
public class UserRoleRequest {
    private Role role;
}
