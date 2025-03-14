package com.tahaakocer.camunda.config;

import camundajar.impl.com.google.gson.Gson;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class GsonConfig {

    @Bean
    public Gson gson() {
        return new Gson();
    }
}
