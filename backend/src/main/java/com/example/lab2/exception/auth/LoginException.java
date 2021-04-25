package com.example.lab2.exception.auth;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class LoginException extends RuntimeException {
    public LoginException(String s) {
        super(s);
    }
}

