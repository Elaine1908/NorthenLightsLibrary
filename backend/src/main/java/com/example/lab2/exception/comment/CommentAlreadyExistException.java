package com.example.lab2.exception.comment;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CommentAlreadyExistException extends RuntimeException {
    public CommentAlreadyExistException(String s){super(s);}
}
