package com.example.lab2.exceptionhandler;

import com.example.lab2.controller.SearchController;
import com.example.lab2.exception.BookTypeNotFoundException;
import com.example.lab2.response.GeneralResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(assignableTypes = {SearchController.class})
public class SearchExceptionHandler {

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<GeneralResponse> illegalArgumentExceptionHandler(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GeneralResponse(e.getMessage()));
    }

    @ExceptionHandler(value = BookTypeNotFoundException.class)
    public ResponseEntity<GeneralResponse> bookTypeNotFoundExceptionHandler(Exception e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new GeneralResponse(e.getMessage()));
    }

}
