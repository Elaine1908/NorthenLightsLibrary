package com.example.lab2.exceptionhandler;


import com.example.lab2.controller.ReserveController;
import com.example.lab2.exception.ReserveException;
import com.example.lab2.response.GeneralResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(assignableTypes = {ReserveController.class})
public class ReserveExceptionHandler {


    @ExceptionHandler(value = ReserveException.class)
    public ResponseEntity<GeneralResponse> handleReserveException(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GeneralResponse(e.getMessage()));
    }
}
