package com.example.lab2.exceptionhandler;

import com.example.lab2.controller.*;
import com.example.lab2.exception.notfound.NotFoundException;
import com.example.lab2.filter.JwtAuthenticationFilter;
import com.example.lab2.filter.JwtLoginFilter;
import com.example.lab2.filter.MakeInStreamRereadableFilter;
import com.example.lab2.interceptor.CommentReplyInterceptor;
import com.example.lab2.interceptor.UserCreditInterceptor;
import com.example.lab2.response.GeneralResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(assignableTypes = {
        AuthController.class,
        AdminController.class,
        BorrowController.class,
        InitializeController.class,
        NormalUserController.class,
        ReserveController.class,
        SearchController.class,
        SuperAdminController.class,
        UserAdminController.class,
        CommentReplyInterceptor.class,
        UserCreditInterceptor.class,
        JwtAuthenticationFilter.class,
        JwtLoginFilter.class,
        MakeInStreamRereadableFilter.class})
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<GeneralResponse> globalExceptionHandler(Exception e) {
        if (e instanceof NotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new GeneralResponse(e.getMessage())
            );
        }


        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new GeneralResponse(e.getMessage())
        );

    }


}
