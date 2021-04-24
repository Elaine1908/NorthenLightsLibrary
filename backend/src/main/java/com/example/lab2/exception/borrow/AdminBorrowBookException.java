package com.example.lab2.exception.borrow;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AdminBorrowBookException extends RuntimeException{
    public AdminBorrowBookException(String s) {
        super(s);
    }
}
