package com.example.lab2.exception.notfound;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class LibraryNotFoundException extends RuntimeException implements NotFoundException {

    public LibraryNotFoundException(String s) {
        super(s);
    }
}
