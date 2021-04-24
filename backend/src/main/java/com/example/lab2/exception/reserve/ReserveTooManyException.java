package com.example.lab2.exception.reserve;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ReserveTooManyException extends RuntimeException {
    public ReserveTooManyException(String s) {
        super(s);
    }
}
