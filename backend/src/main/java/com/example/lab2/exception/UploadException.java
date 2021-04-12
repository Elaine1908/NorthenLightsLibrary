package com.example.lab2.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UploadException extends RuntimeException {
    public UploadException(String s) {
        super(s);
    }
}
