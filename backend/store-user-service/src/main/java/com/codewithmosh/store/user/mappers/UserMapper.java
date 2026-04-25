package com.codewithmosh.store.user.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.codewithmosh.store.user.dto.CreateUserRequest;
import com.codewithmosh.store.user.dto.UpdateUserRequest;
import com.codewithmosh.store.user.dto.UserDto;
import com.codewithmosh.store.user.entities.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
    User toUser(CreateUserRequest request);
    void updateUser(UpdateUserRequest request, @MappingTarget User user);
}
