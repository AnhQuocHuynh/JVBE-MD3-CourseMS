package com.example.coursems.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserStatusRequest {
    @NotNull(message = "Trang thai hoat dong khong duoc de trong")
    private Boolean isActive;
}
