package com.example.lab2.exception.reserve;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AdminReserveBookException extends RuntimeException{

    public AdminReserveBookException(String message) {
        super(message);
    }
}
