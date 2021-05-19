package com.example.lab2.exception.notfound;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class FineNotFoundException extends RuntimeException{
    public FineNotFoundException(String message) {
        super(message);
    }
}
