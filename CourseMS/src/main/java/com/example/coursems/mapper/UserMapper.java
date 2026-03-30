package com.example.coursems.mapper;

import com.example.coursems.dto.response.UserResponse;
import com.example.coursems.entity.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @BeanMapping(builder = @Builder(disableBuilder = true))
    @Mapping(source = "userId", target = "id")
    @Mapping(source = "active", target = "active")
    UserResponse toResponse(User user);
}
