package com.example.coursems.mapper;

import com.example.coursems.dto.response.UserResponse;
import com.example.coursems.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(source = "userId", target = "id")
    @Mapping(source = "active", target = "isActive")
    UserResponse toResponse(User user);
}
