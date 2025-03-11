package com.tahaakocer.user_service.config;

import com.tahaakocer.user_service.Soap.MNHKPSPublicSoap;
import com.tahaakocer.user_service.dto.GeneralResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SoapConfig {

    @Bean
    MNHKPSPublicSoap mnhKPSPublicSoap() {
        return new MNHKPSPublicSoap();
    }
}
