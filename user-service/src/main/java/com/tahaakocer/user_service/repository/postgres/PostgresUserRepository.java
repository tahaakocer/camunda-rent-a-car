package com.tahaakocer.user_service.repository.postgres;

import com.tahaakocer.user_service.model.postgres.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PostgresUserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByKeycloakUserId(String keycloakUserId);
    Optional<User> findByEmail(String email);

    Boolean existsByTCNo(String tcNo);
}
