package com.example.lab2.exception.auth;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PasswordException extends RuntimeException {
    public PasswordException(String s) {
        super(s);
    }
}
