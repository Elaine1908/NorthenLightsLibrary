package com.example.lab2.exception.borrow;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NotBorrowedException extends RuntimeException{
    public NotBorrowedException(String s) {
        super(s);
    }
}
