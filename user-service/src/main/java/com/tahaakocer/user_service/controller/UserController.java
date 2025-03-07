package com.tahaakocer.user_service.controller;

import com.tahaakocer.user_service.dto.EmailVerificationDto;
import com.tahaakocer.user_service.dto.GeneralResponse;
import com.tahaakocer.user_service.dto.UserRequest;
import com.tahaakocer.user_service.mapper.UserMapper;
import com.tahaakocer.user_service.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/users")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping("/register")
    public ResponseEntity<GeneralResponse> registerUser(UserRequest userRequest) {
        return ResponseEntity.ok(GeneralResponse.builder()
                .code(200)
                .message("User registered successfully")
                .data(this.userMapper.dtoToResponse(
                            this.userService.registerUser(this.userMapper.requestToDto(userRequest))))
                .build()
        );
    }

    @PostMapping("/send-verification-email")
    public ResponseEntity<GeneralResponse> sendVerificationEmail(String keycloakUserId) {
        this.userService.sendVerificationEmail(keycloakUserId);
        return ResponseEntity.ok(GeneralResponse.builder()
                .code(200)
                .message("Verification email sent successfully")
                .build()
        );
    }
    @PostMapping("/check-and-update-email-verification-status")
    public ResponseEntity<GeneralResponse> checkAndUpdateEmailVerificationStatus(String keycloakUserId) {
        EmailVerificationDto emailVerificationDto = this.userService.checkAndUpdateEmailVerificationStatus(keycloakUserId);
        return ResponseEntity.ok(GeneralResponse.builder()
                .code(200)
                .message("Email verification status checked and updated successfully")
                        .data(emailVerificationDto)
                .build()
        );
    }
}
