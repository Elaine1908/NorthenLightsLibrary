package com.example.lab2.exception.borrow;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class BorrowToManyException extends RuntimeException{
    public BorrowToManyException(String message) {
        super(message);
    }
}
