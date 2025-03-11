package com.tahaakocer.user_service.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.tahaakocer.user_service.model.enums.UserStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class UserResponse {
    private String id;
    private String tcNo;
    private String firstName;
    private String lastName;
    private int birthYear;
    private String phoneNumber;
    private String email;
    private String keycloakUserId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserStatus status;
}
