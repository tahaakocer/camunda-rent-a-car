package com.tahaakocer.user_service.mapper;
import com.tahaakocer.user_service.dto.UserPostgresRequest;
import com.tahaakocer.user_service.dto.UserRequest;
import com.tahaakocer.user_service.dto.UserResponse;
import com.tahaakocer.user_service.dto.UserDto;
import com.tahaakocer.user_service.model.mongodb.User;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    //MONGODB ENTITY
    User dtoToMongoUser(UserDto userDto);
    UserDto mongoUserToDto(User user);

    //POSTGRES ENTITY
    com.tahaakocer.user_service.model.postgres.User dtoToPostgresUser(UserDto userDto);
    UserDto postgresUserToDto(com.tahaakocer.user_service.model.postgres.User user);

    UserDto requestToDto(UserRequest userRequest);
    UserResponse dtoToResponse(UserDto userDto);

    UserDto postgresRequestToDto(UserPostgresRequest userPostgresRequest);
}
