package com.example.demo.lemoncash.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Data
@Configuration
@ConfigurationProperties(prefix = "db.datasource")
public class DataSourceConfig extends HikariConfig {

    private String db;
    private String url;
    private String password;

    @Bean
    @SneakyThrows
    public DataSource getDataSource() {
        String jdbcUrl = String.format("%s/%s", url, db);
        setJdbcUrl(jdbcUrl);
        setPassword(password);
        return new HikariDataSource(this);
    }

}