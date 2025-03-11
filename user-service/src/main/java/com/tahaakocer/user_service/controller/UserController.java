package com.tahaakocer.user_service.controller;

import com.tahaakocer.user_service.dto.EmailVerificationDto;
import com.tahaakocer.user_service.dto.GeneralResponse;
import com.tahaakocer.user_service.dto.UserDto;
import com.tahaakocer.user_service.dto.UserRequest;
import com.tahaakocer.user_service.mapper.UserMapper;
import com.tahaakocer.user_service.service.MernisService;
import com.tahaakocer.user_service.service.UserService;
import com.tahaakocer.user_service.service.ValidationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/users")
public class UserController {
    private final UserService userService;
    private final ValidationService validationService;
    private final MernisService mernisService;
    private final UserMapper userMapper;


    public UserController(UserService userService, ValidationService validationService, MernisService mernisService, UserMapper userMapper) {
        this.userService = userService;
        this.validationService = validationService;
        this.mernisService = mernisService;
        this.userMapper = userMapper;
    }

    @PostMapping("/register-h2")
    public ResponseEntity<GeneralResponse> registerUserH2(@RequestBody UserRequest userRequest) {
        return ResponseEntity.ok(GeneralResponse.builder()
                .code(200)
                .message("User registered h2 successfully")
                .data(this.userMapper.dtoToResponse(
                            this.userService.registerUserH2(this.userMapper.requestToDto(userRequest))))
                .build()
        );
    }
    @PostMapping("/register-postgres")
    public ResponseEntity<GeneralResponse> registerUserPostgres(@RequestBody UserRequest userRequest) {
        return ResponseEntity.ok(GeneralResponse.builder()
                .code(200)
                .message("User registered postgres successfully")
                .data(this.userMapper.dtoToResponse(
                        this.userService.registerUserPostgres(this.userMapper.requestToDto(userRequest))))
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
    @PostMapping("/is-form-valid")
    public ResponseEntity<GeneralResponse> isFormValid(@RequestBody UserRequest userRequest) {

        return ResponseEntity.ok(GeneralResponse.builder()
                .code(200)
                .message("Form validation result")
                        .data(this.validationService.isFormValid(this.userMapper.requestToDto(userRequest)))
                .build()
        );
    }
    @PostMapping("/is-mernis-valid")
    public ResponseEntity<GeneralResponse> isMernisValid(@RequestBody UserRequest userRequest) {

        return ResponseEntity.ok(GeneralResponse.builder()
                .code(200)
                .message("Mernis validation result")
                            .data(this.mernisService.checkIfRealPerson(this.userMapper.requestToDto(userRequest)))
                .build()
        );
    }
}
