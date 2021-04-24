package com.example.lab2.exception.reserve;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ReservedByOtherException extends RuntimeException  {
    public ReservedByOtherException(String s) {
        super(s);
    }
}
