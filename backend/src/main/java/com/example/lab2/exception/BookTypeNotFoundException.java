package com.example.lab2.exception;

/**
 * 搜索书本时书本没找到的异常
 */
public class BookTypeNotFoundException extends RuntimeException {
    public BookTypeNotFoundException() {
    }

    public BookTypeNotFoundException(String s) {
        super(s);
    }
}
