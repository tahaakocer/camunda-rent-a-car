package com.tahaakocer.user_service.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import jakarta.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.tahaakocer.user_service.repository.h2",
        entityManagerFactoryRef = "h2EntityManagerFactory",
        transactionManagerRef = "h2TransactionManager"
)
public class H2DataSourceConfig {

    @Value("${spring.datasource.h2.url}")
    private String h2Url;
    @Value("${spring.datasource.h2.username}")
    private String h2Username;
    @Value("${spring.datasource.h2.password}")
    private String h2Password;
    @Value("${spring.datasource.h2.driver-class-name}")
    private String h2DriverClassName;

    @Bean(name = "h2DataSource")
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .url(h2Url)
                .username(h2Username)
                .password(h2Password)
                .driverClassName(h2DriverClassName)
                .build();
    }

    @Bean(name = "h2EntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean h2EntityManagerFactory(
            @Qualifier("h2DataSource") DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.tahaakocer.user_service.model.h2");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean(name = "h2TransactionManager")
    public PlatformTransactionManager h2TransactionManager(
            @Qualifier("h2EntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}