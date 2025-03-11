package com.tahaakocer.user_service.service;

import com.tahaakocer.user_service.dto.UserDto;
import com.tahaakocer.user_service.dto.ValidFormResponse;
import org.springframework.stereotype.Service;

@Service
public class ValidationService {


    public ValidFormResponse isFormValid(UserDto userDto) {
        if (userDto == null) {
            return ValidFormResponse.builder().isFormValid(false).build();
        }
        if (userDto.getTcNo() == null || !userDto.getTcNo().trim().matches("\\d{11}")) {
            return ValidFormResponse.builder().isFormValid(false).build();
        }
        if (userDto.getFirstName() == null || userDto.getFirstName().trim().isEmpty()) {
            return ValidFormResponse.builder().isFormValid(false).build();
        }
        if (userDto.getLastName() == null || userDto.getLastName().trim().isEmpty()) {
            return ValidFormResponse.builder().isFormValid(false).build();
        }
        if (userDto.getPhoneNumber() == null || !userDto.getPhoneNumber().matches("\\+?\\d+")) {
            return ValidFormResponse.builder().isFormValid(false).build();
        }
        if (userDto.getEmail() == null || !userDto.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            return ValidFormResponse.builder().isFormValid(false).build();
        }
        if (userDto.getPassword() == null || userDto.getPassword().length() < 8) {
            return ValidFormResponse.builder().isFormValid(false).build();
        }
        return ValidFormResponse.builder().isFormValid(true).build();
    }
}
