package com.tahaakocer.user_service.repository.mongodb;

import com.tahaakocer.user_service.model.mongodb.User;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MongoUserRepository extends MongoRepository<User, String> {
    Optional<User> findByKeycloakUserId(String keycloakUserId);
    Optional<User> findByEmail(String email);
    Boolean existsByTcNo(String tcNo);
}
