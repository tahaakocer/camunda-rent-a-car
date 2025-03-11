package com.tahaakocer.user_service.mapper;
import com.tahaakocer.user_service.dto.UserRequest;
import com.tahaakocer.user_service.dto.UserResponse;
import com.tahaakocer.user_service.dto.UserDto;
import com.tahaakocer.user_service.model.mongodb.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User dtoToUser(UserDto userDto);

    UserDto userToDto(User user);

    UserDto requestToDto(UserRequest userRequest);

    UserResponse dtoToResponse(UserDto userDto);

}
