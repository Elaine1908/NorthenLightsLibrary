package com.example.lab2.exception.bookcopy;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class BookCopyIsBorrowedException extends RuntimeException {
    public BookCopyIsBorrowedException(String s) {
        super(s);
    }
}
