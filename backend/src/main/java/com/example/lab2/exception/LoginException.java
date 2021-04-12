package com.example.lab2.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class LoginException extends RuntimeException {
    public LoginException(String s) {
        super(s);
    }
}

