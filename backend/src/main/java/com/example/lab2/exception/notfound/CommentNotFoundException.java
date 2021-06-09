package com.example.lab2.exception.notfound;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CommentNotFoundException extends RuntimeException implements NotFoundException {
    public CommentNotFoundException(String s) {
        super(s);
    }
}
