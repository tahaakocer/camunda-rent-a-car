package com.tahaakocer.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class UserRequest
{
    private String TCNo;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String password;
}
