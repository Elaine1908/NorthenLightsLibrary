package com.example.lab2.exception.bookcopy;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class BookCopyNotHereException extends RuntimeException {
    public BookCopyNotHereException(String s) {
        super(s);
    }
}
