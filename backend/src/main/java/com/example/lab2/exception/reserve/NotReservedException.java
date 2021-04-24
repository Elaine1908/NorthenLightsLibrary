package com.example.lab2.exception.reserve;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NotReservedException extends RuntimeException{

    public NotReservedException(String s) {
        super(s);
    }
}
