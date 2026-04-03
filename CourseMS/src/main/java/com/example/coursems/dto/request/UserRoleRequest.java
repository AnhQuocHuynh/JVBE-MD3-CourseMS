package com.example.coursems.dto.request;

import com.example.coursems.entity.Role;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserRoleRequest {
    @NotNull(message = "Vai tro khong duoc de trong")
    private Role role;
}
