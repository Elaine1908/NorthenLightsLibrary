package com.example.lab2.exception.notfound;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class BookCopyNotFoundException extends RuntimeException
implements NotFoundException {
    public BookCopyNotFoundException(String s) {
        super(s);
    }
}
