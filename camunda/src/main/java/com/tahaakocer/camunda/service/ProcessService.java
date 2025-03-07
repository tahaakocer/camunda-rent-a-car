package com.tahaakocer.camunda.service;

import com.tahaakocer.camunda.exception.StartProcessException;
import org.camunda.bpm.engine.RuntimeService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class ProcessService {
    Logger logger = org.slf4j.LoggerFactory.getLogger(ProcessService.class);
    private final RuntimeService runtimeService;
    private final RestTemplate restTemplate;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.username}")
    private String username;

    @Value("${keycloak.password}")
    private String password;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    @Value("${keycloak.token.url}")
    private String tokenUrl;

    public ProcessService(RuntimeService runtimeService, RestTemplate restTemplate) {
        this.runtimeService = runtimeService;
        this.restTemplate = restTemplate;
    }

    public void startProcess(String processKey) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
            form.add("grant_type", "password");
            form.add("client_id", clientId);
            form.add("username", username);
            form.add("password", password);
            form.add("client_secret", clientSecret);

            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(form, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, requestEntity, Map.class);
            Map body = response.getBody();
            if (body == null || !body.containsKey("access_token")) {
                throw new RuntimeException("Keycloak token alınamadı.");
            }
            String accessToken = body.get("access_token").toString();
            String refreshToken = body.get("refresh_token").toString();

            Map<String, Object> variables = new HashMap<>();
            variables.put("accessToken", accessToken);
            variables.put("refreshToken", refreshToken);

            variables.put("refreshUrl", tokenUrl);

            runtimeService.startProcessInstanceByKey(processKey, variables);
        } catch (Exception e) {
            logger.error("Process başlatılırken hata oluştu.", e);

            throw new StartProcessException("Process başlatılırken hata oluştu: " + e.getMessage(), e);
        }
    }
}
