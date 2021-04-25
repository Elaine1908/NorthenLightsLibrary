package com.example.lab2.exception.auth;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RegisterException extends RuntimeException {
    public RegisterException(String s) {
        super(s);
    }
}
