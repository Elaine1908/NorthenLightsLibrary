package com.example.lab2.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RegisterException extends RuntimeException {
    public RegisterException(String s) {
        super(s);
    }
}
