package com.tahaakocer.user_service.service;

import com.tahaakocer.user_service.dto.EmailVerificationDto;
import com.tahaakocer.user_service.dto.UserDto;
import com.tahaakocer.user_service.exception.GeneralException;
import com.tahaakocer.user_service.mapper.UserMapper;
import com.tahaakocer.user_service.model.h2.User;
import com.tahaakocer.user_service.model.enums.UserStatus;
import com.tahaakocer.user_service.repository.h2.H2UserRepository;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class UserService {
    private final Keycloak keycloak;
    private final H2UserRepository h2UserRepository;
    private final UserMapper userMapper;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.resource}")
    private String clientId;

    public UserService(Keycloak keycloak, H2UserRepository h2UserRepository, UserMapper userMapper) {
        this.keycloak = keycloak;
        this.h2UserRepository = h2UserRepository;
        this.userMapper = userMapper;
    }

    @Transactional
    public UserDto registerUser(UserDto userDto) {
        Boolean userExists = this.h2UserRepository.existsByTCNo(userDto.getEmail());
        if (userExists != null) {
            log.error("UserService - registerUser - Kullanıcı zaten kayıtlı");
            throw new GeneralException("Kullanıcı zaten kayıtlı");
        }
        try {
            String keycloakUserId = createKeycloakUser(
                    keycloak,
                    userDto.getFirstName(),
                    userDto.getLastName(),
                    userDto.getEmail(),
                    userDto.getPassword()
            );
            assignClientRoleToUser(keycloak, keycloakUserId, "user");
            User user = this.userMapper.dtoToUser(userDto);
            user.setKeycloakUserId(keycloakUserId);
            user.setStatus(UserStatus.INACTIVE);
            log.info("UserService - registerUser - Kullanıcı kaydedildi");
            return this.userMapper.userToDto(h2UserRepository.save(user));
        } catch (Exception e) {
            log.error("UserService - registerUser - Kullanıcı kaydı sırasında hata oluştu", e);
            throw new GeneralException("Kullanıcı kaydı sırasında hata oluştu", e);
        }
    }

    private String createKeycloakUser(Keycloak keycloak, String firstName, String lastName,
                                      String email, String password) {

        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();

        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setEmailVerified(false);

        List<String> requiredActions = new ArrayList<>();
        requiredActions.add("VERIFY_EMAIL");
        user.setRequiredActions(requiredActions);
        Response response = usersResource.create(user);
        if (response.getStatus() != 201) {
            log.error("Keycloak'ta kullanıcı oluşturulamadı. Status: {}", response.getStatus());
            throw new GeneralException("Keycloak'ta kullanıcı oluşturulamadı. Status: " + response.getStatus());
        }
        String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
        setUserPassword(realmResource.users().get(userId), password);
        log.info("Keycloak'ta kullanıcı oluşturuldu. Kullanıcı ID: {}", userId);
        return userId;
    }

    private void setUserPassword(UserResource userResource, String password) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        userResource.resetPassword(credential);
    }

    private void assignClientRoleToUser(Keycloak keycloak, String userId, String roleName) {
        RealmResource realmResource = keycloak.realm(realm);
        UserResource userResource = realmResource.users().get(userId);
        String clientUuid = realmResource.clients().findByClientId(clientId).getFirst().getId();
        RoleRepresentation clientRole = realmResource.clients().get(clientUuid)
                .roles().get(roleName).toRepresentation();
        userResource.roles().clientLevel(clientUuid).add(Collections.singletonList(clientRole));
    }

    private void assignRealmRoleToUser(Keycloak keycloak, String userId, String roleName) {
        RealmResource realmResource = keycloak.realm(realm);
        UserResource userResource = realmResource.users().get(userId);
        RoleRepresentation realmRole = realmResource.rolesById().getRole(roleName);
        userResource.roles().realmLevel().add(Collections.singletonList(realmRole));
    }

    public void sendVerificationEmail(String userId) {
        RealmResource realmResource = keycloak.realm(realm);
        UserResource userResource = realmResource.users().get(userId);
        try {
            userResource.sendVerifyEmail();
        } catch (Exception e) {
            log.error("E-posta doğrulama maili gönderilemedi", e);
            throw new GeneralException("E-posta doğrulama maili gönderilemedi", e);
        }
    }

    public EmailVerificationDto checkAndUpdateEmailVerificationStatus(String keycloakUserId) {
        RealmResource realmResource = keycloak.realm(realm);
        UserRepresentation userRep = realmResource.users().get(keycloakUserId).toRepresentation();
        boolean isEmailVerified = userRep.isEmailVerified();
        if (isEmailVerified) {
            User user = h2UserRepository.findByKeycloakUserId(keycloakUserId)
                    .orElseThrow(() -> new GeneralException("Kullanıcı bulunamadı"));

            user.setStatus(UserStatus.ACTIVE);
            h2UserRepository.save(user);
        }
        return EmailVerificationDto.builder()
                .isEmailVerified(isEmailVerified)
                .build();
    }
}
