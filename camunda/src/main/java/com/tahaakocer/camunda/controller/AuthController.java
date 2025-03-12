package com.tahaakocer.camunda.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class AuthController {

    private static final String TOKEN_URL = "http://localhost:8083/realms/camunda-rent-a-car/protocol/openid-connect/token";
    private static final String CLIENT_ID = "rest-api";
    private static final String CLIENT_SECRET = "fJyHtl6X0qKC4hGUIywWQS8iCmyECY3V";
    private static final String GRANT_TYPE = "password";
    private static final String USERNAME = "user1";
    private static final String PASSWORD = "123456";

    @GetMapping("/api/token")
    public ResponseEntity<String> getAccessToken() {
        RestTemplate restTemplate = new RestTemplate();

        // İstek başlıkları
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // İstek gövdesi
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", CLIENT_ID);
        body.add("client_secret", CLIENT_SECRET);
        body.add("grant_type", GRANT_TYPE);
        body.add("username", USERNAME);
        body.add("password", PASSWORD);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        // Token isteği
        ResponseEntity<String> response = restTemplate.postForEntity(TOKEN_URL, request, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok(response.getBody()); // JSON yanıtını doğrudan döndür
        } else {
            return ResponseEntity.status(response.getStatusCode()).body("Failed to fetch token");
        }
    }
}