package com.example.lab2.exception.bookcopy;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class BookCopyReservedException extends RuntimeException{
    public BookCopyReservedException(String s) {
        super(s);
    }
}
