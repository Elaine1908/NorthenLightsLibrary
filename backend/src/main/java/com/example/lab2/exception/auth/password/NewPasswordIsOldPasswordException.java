package com.example.lab2.exception.auth.password;

import com.example.lab2.exception.auth.PasswordException;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NewPasswordIsOldPasswordException extends PasswordException {
    public NewPasswordIsOldPasswordException(String s) {
        super(s);
    }
}
