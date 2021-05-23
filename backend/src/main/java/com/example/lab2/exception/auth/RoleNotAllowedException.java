package com.example.lab2.exception.auth;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RoleNotAllowedException extends RuntimeException {
    public RoleNotAllowedException(String message) {
        super(message);
    }
}
