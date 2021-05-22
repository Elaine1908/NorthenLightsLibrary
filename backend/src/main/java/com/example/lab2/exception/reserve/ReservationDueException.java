package com.example.lab2.exception.reserve;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ReservationDueException extends RuntimeException{
    public ReservationDueException(String message) {
        super(message);
    }
}
