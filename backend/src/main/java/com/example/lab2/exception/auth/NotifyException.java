package com.example.lab2.exception.auth;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NotifyException extends RuntimeException{
    public NotifyException(String message) {
        super(message);
    }
}
