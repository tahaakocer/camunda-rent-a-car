package com.tahaakocer.user_service.service;

import com.tahaakocer.user_service.dto.EmailVerificationDto;
import com.tahaakocer.user_service.dto.UserDto;
import com.tahaakocer.user_service.exception.GeneralException;
import com.tahaakocer.user_service.mapper.UserMapper;
import com.tahaakocer.user_service.model.mongodb.User;
import com.tahaakocer.user_service.model.enums.UserStatus;
import com.tahaakocer.user_service.repository.mongodb.MongoUserRepository;
import com.tahaakocer.user_service.repository.postgres.PostgresUserRepository;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.ClientRepresentation;
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
    private final MongoUserRepository mongoUserRepository;
    private final PostgresUserRepository postgresUserRepository;
    private final UserMapper userMapper;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.resource}")
    private String clientId;

    public UserService(Keycloak keycloak, MongoUserRepository mongoUserRepository, PostgresUserRepository postgresUserRepository, UserMapper userMapper) {
        this.keycloak = keycloak;
        this.mongoUserRepository = mongoUserRepository;
        this.postgresUserRepository = postgresUserRepository;
        this.userMapper = userMapper;
    }

    @Transactional
    public UserDto registerUserMongo(UserDto userDto) {
        // MongoDB'de TC No kontrolü
        boolean userExistsInMongo = mongoUserRepository.existsByTcNo(userDto.getTcNo());

        if (userExistsInMongo) {
            log.error("UserService - registerUser - Kullanıcı zaten kayıtlı (MongoDB)");
            throw new GeneralException("Kullanıcı zaten kayıtlı (MongoDB)");
        }

            String keycloakUserId = createKeycloakUser(
                    keycloak,
                    userDto.getFirstName(),
                    userDto.getLastName(),
                    userDto.getEmail(),
                    userDto.getPassword()
            );
            assignClientRoleToUser(keycloak, keycloakUserId, "user");
            User user = userMapper.dtoToMongoUser(userDto);
            user.setKeycloakUserId(keycloakUserId);
            user.setStatus(UserStatus.INACTIVE);
            try {
                UserDto savedUserDto = userMapper.mongoUserToDto(mongoUserRepository.save(user));
                log.info("UserService - registerUser - Kullanıcı kaydedildi");
                return savedUserDto;

            }catch (Exception e) {
                throw new GeneralException("Kullanıcı mongo kaydı sırasında hata oluştu", e);
            }


    }

    @Transactional
    public UserDto registerUserPostgres(UserDto userDto) {
        // Bu kısım Postgres veri kaynağına ait işlemleri yürütüyor.
        Boolean userExists = this.postgresUserRepository.existsByTcNo(userDto.getEmail());
        if (userExists) {
            log.error("UserService - registerUser - Kullanıcı zaten kayıtlı");
            throw new GeneralException("Kullanıcı zaten kayıtlı");
        }
        try {
            com.tahaakocer.user_service.model.postgres.User user = userMapper.dtoToPostgresUser(userDto);
            com.tahaakocer.user_service.model.postgres.User saved = this.postgresUserRepository.save(user);
            return userMapper.postgresUserToDto(saved);
        } catch (Exception e) {
            log.error("UserService - registerUser - Kullanıcı kaydı sırasında hata oluştu", e);
            throw new GeneralException("Kullanıcı postgres kaydı sırasında hata oluştu", e);
        }
    }

    private String createKeycloakUser(Keycloak keycloak, String firstName, String lastName,
                                      String email, String password) {
        log.info("Creating Keycloak user with realm: {}, clientId: {}", realm, clientId);
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();

        // Kullanıcı adı
        List<UserRepresentation> existingUsers = usersResource.searchByUsername(email, true);
        if (!existingUsers.isEmpty()) {
            log.error("Kullanıcı adı zaten mevcut: {}", email);
            throw new GeneralException("Kullanıcı adı zaten mevcut.");
        }

        // E-posta
        List<UserRepresentation> existingEmails = usersResource.searchByEmail(email, true);
        if (!existingEmails.isEmpty()) {
            log.error("E-posta adresi zaten mevcut: {}", email);
            throw new GeneralException("E-posta adresi zaten mevcut.");
        }

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
        String userId;

        if (response.getStatus() == 201) {
            userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
            log.info("Kullanıcı başarıyla oluşturuldu (Status 201). Kullanıcı ID: {}", userId);
        } else {
            List<UserRepresentation> createdUsers = usersResource.searchByUsername(email, true);
            if (!createdUsers.isEmpty()) {
                userId = createdUsers.get(0).getId();
                log.warn("Kullanıcı oluşturuldu ancak beklenmeyen durum kodu alındı: {}. Kullanıcı ID: {}", response.getStatus(), userId);
            } else if (response.getStatus() == 409) {
                log.error("Kullanıcı adı veya e-posta zaten mevcut.");
                throw new GeneralException("Kullanıcı adı veya e-posta zaten mevcut.");
            } else {
                String errorMessage = response.readEntity(String.class); // Hata mesajını al
                log.error("Keycloak'ta kullanıcı oluşturulamadı. Status: {}, Mesaj: {}", response.getStatus(), errorMessage);
                throw new GeneralException("Keycloak'ta kullanıcı oluşturulamadı. Status: " + response.getStatus() + ", Mesaj: " + errorMessage);
            }
        }

        setUserPassword(realmResource.users().get(userId), password);
        log.info("Keycloak'ta kullanıcı oluşturuldu ve şifre ayarlandı. Kullanıcı ID: {}", userId);
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
        try {
            RealmResource realmResource = keycloak.realm(realm);
            UserResource userResource = realmResource.users().get(userId);
            List<ClientRepresentation> clients = realmResource.clients().findByClientId(clientId);
            String clientUuid = clients.get(0).getId();
            RoleRepresentation clientRole = realmResource.clients().get(clientUuid)
                    .roles().get(roleName).toRepresentation();
            userResource.roles().clientLevel(clientUuid).add(Collections.singletonList(clientRole));
        } catch (Exception e) {
            throw new GeneralException("Kullanıcı client rol ataması sırasında hata oluştu", e);
        }

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
            User user = mongoUserRepository.findByKeycloakUserId(keycloakUserId)
                    .orElseThrow(() -> new GeneralException("Kullanıcı bulunamadı"));

            user.setStatus(UserStatus.ACTIVE);
            userRep.setEnabled(true);
            mongoUserRepository.save(user);
        }
        return EmailVerificationDto.builder()
                .isEmailVerified(isEmailVerified)
                .build();
    }
}
