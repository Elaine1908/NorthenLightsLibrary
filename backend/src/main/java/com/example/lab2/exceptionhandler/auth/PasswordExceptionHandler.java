package com.example.lab2.exceptionhandler.auth;

import com.example.lab2.controller.UserController;
import com.example.lab2.exception.PasswordException;
import com.example.lab2.response.GeneralResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(assignableTypes = UserController.class)
public class PasswordExceptionHandler {

    @ExceptionHandler(value = PasswordException.class)
    public ResponseEntity<GeneralResponse> passwordExceptionHandler(PasswordException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GeneralResponse(e.getMessage()));
    }

}
