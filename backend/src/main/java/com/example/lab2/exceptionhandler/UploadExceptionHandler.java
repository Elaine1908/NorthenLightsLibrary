package com.example.lab2.exceptionhandler;


import com.example.lab2.controller.AdminController;
import com.example.lab2.exception.UploadException;
import com.example.lab2.response.GeneralResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice(assignableTypes = {AdminController.class})
public class UploadExceptionHandler {


    /**
     * 处理UploadException异常，给出提示
     *
     * @param e UploadException异常
     * @return 提醒用户出了什么异常
     */
    @ExceptionHandler(value = UploadException.class)
    public ResponseEntity<GeneralResponse> uploadExceptionHandler(UploadException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new GeneralResponse(e.getMessage())
        );
    }


}
