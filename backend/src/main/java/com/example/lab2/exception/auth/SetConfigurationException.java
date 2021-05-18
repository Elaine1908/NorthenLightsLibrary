package com.example.lab2.exception.auth;


import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SetConfigurationException extends RuntimeException {
    public SetConfigurationException(String s) {
        super(s);
    }
}
