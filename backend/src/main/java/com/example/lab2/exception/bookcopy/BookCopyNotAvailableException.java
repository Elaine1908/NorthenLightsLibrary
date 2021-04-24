package com.example.lab2.exception.bookcopy;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class BookCopyNotAvailableException extends RuntimeException  {
    public BookCopyNotAvailableException(String s) {
        super(s);
    }
}
