package com.example.lab2.exception.auth.password;

import com.example.lab2.exception.auth.PasswordException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class WrongPasswordException extends PasswordException {
    public WrongPasswordException(String s) {
        super(s);
    }
}
