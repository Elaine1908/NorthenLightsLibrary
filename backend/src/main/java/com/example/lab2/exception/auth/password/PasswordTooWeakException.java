package com.example.lab2.exception.auth.password;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PasswordTooWeakException extends PasswordException{
    public PasswordTooWeakException(String s) {
        super(s);
    }
}
