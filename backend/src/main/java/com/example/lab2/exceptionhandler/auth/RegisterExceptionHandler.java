package com.example.lab2.exceptionhandler.auth;

import com.example.lab2.controller.UserController;
import com.example.lab2.exception.RegisterException;
import com.example.lab2.response.GeneralResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice(assignableTypes = UserController.class)
public class RegisterExceptionHandler {

    /**
     * 处理注册过程中，抛出的RegisterException异常，并给出信息
     *
     * @param e RegisterException异常
     * @return info
     */
    @ExceptionHandler(value = RegisterException.class)
    public ResponseEntity<GeneralResponse> registerExceptionHandler(RegisterException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GeneralResponse(e.getMessage()));
    }


}
