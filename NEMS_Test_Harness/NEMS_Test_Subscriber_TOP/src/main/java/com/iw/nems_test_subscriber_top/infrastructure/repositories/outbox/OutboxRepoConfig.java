package com.iw.nems_test_subscriber_top.infrastructure.repositories.outbox;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
@EnableJpaRepositories("com.iw.nems_test_subscriber_top.infrastructure.repositories.outbox")
public class OutboxRepoConfig {

    @Autowired
    private Environment environment;

    private String propPrefix = "spring.datasource.";

    @Bean
    @Primary
    @Qualifier("outboxDataSource")
    public DataSource getDataSource() {
        @SuppressWarnings("rawtypes")
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName(environment.getProperty(propPrefix + "driver-class-name"));
        dataSourceBuilder.url(environment.getProperty(propPrefix + "url"));
        dataSourceBuilder.username(environment.getProperty(propPrefix + "username"));
        dataSourceBuilder.password(environment.getProperty(propPrefix + "password"));
        System.out.println(dataSourceBuilder.toString());
        return dataSourceBuilder.build();
    }

    @Bean
    @Primary
    NamedParameterJdbcOperations jdbcOperations( @Qualifier("outboxDataSource") DataSource sqlServerDs) {
        return new NamedParameterJdbcTemplate(sqlServerDs);
  }
}
