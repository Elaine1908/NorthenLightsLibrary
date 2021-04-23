package com.example.lab2.exception.notfound;

/**
 * 搜索书本时书本没找到的异常
 */
public class BookTypeNotFoundException extends RuntimeException implements NotFoundException {
    public BookTypeNotFoundException() {
    }

    public BookTypeNotFoundException(String s) {
        super(s);
    }
}
