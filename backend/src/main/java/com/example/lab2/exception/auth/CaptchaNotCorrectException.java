package com.example.lab2.exception.auth;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CaptchaNotCorrectException extends RuntimeException {
    public CaptchaNotCorrectException(String message) {
        super(message);
    }
}
