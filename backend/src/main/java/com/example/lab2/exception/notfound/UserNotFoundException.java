package com.example.lab2.exception.notfound;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserNotFoundException extends RuntimeException implements NotFoundException {
    public UserNotFoundException(String s) {
        super(s);
    }
}
