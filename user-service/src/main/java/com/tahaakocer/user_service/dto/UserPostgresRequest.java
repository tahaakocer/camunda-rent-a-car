package com.tahaakocer.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserPostgresRequest {
    private String keycloakUserId;
    private String status;
    private String tcNo;
    private String firstName;
    private String lastName;
    private String birthYear;
    private String phoneNumber;
    private String email;
    private String password;
}
