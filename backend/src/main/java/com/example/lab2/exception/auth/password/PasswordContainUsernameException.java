package com.example.lab2.exception.auth.password;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PasswordContainUsernameException extends PasswordException{
    public PasswordContainUsernameException(String s) {
        super(s);
    }
}
