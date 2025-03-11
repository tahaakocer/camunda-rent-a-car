package com.tahaakocer.user_service.model.mongodb;

import com.tahaakocer.user_service.model.enums.UserStatus;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    private String id; // UUID.toString() şeklinde atayabilirsiniz

    @Field("firstName")
    private String firstName;

    @Field("lastName")
    private String lastName;

    private String phoneNumber;

    @Field("TCNo")
    private String TCNo;

    private String email;

    private String keycloakUserId;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private UserStatus status;
}
