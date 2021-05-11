package com.example.lab2.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "email")
@Getter
@Setter
public class EmailConfig {
    private String username;
    private String password;
    private String smtp_server;



}
