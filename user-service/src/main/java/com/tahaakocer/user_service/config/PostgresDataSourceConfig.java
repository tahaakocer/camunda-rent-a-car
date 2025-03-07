package com.tahaakocer.user_service.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.jdbc.DataSourceBuilder;
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
        basePackages = "com.tahaakocer.user_service.repository.postgres",
        entityManagerFactoryRef = "postgresEntityManagerFactory",
        transactionManagerRef = "postgresTransactionManager"
)
public class PostgresDataSourceConfig {


    @Value("${spring.datasource.postgres.url}")
    private String postgresUrl;

    @Value("${spring.datasource.postgres.username}")
    private String postgresUsername;

    @Value("${spring.datasource.postgres.password}")
    private String postgresPassword;

    @Value("${spring.datasource.postgres.driver-class-name}")
    private String postgresDriverClassName;


    @Primary
    @Bean(name = "postgresDataSource")
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .url(postgresUrl)
                .username(postgresUsername)
                .password(postgresPassword)
                .driverClassName(postgresDriverClassName)
                .build();
    }

    @Primary
    @Bean(name = "postgresEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean postgresEntityManagerFactory(
            @Qualifier("postgresDataSource") DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.tahaakocer.user_service.model.postgres");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Primary
    @Bean(name = "postgresTransactionManager")
    public PlatformTransactionManager postgresTransactionManager(
            @Qualifier("postgresEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}