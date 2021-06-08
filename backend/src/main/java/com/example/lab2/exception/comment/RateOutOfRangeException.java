package com.example.lab2.exception.comment;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RateOutOfRangeException extends RuntimeException{
    public RateOutOfRangeException(String s){super(s);}
}
