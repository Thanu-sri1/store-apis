package com.codewithmosh.store.auth.mappers;

import org.mapstruct.Mapper;

import com.codewithmosh.store.auth.dto.UserDto;
import com.codewithmosh.store.auth.entities.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
}
