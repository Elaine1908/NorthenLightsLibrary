package com.example.lab2.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ReserveException extends RuntimeException {
    public ReserveException(String s) {
        super(s);
    }
}
