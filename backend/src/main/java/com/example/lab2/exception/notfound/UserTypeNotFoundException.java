package com.example.lab2.exception.notfound;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserTypeNotFoundException extends RuntimeException implements NotFoundException {
    public UserTypeNotFoundException(String s) {
        super(s);
    }
}
