package com.example.lab2.dto.bookcopy;

import java.util.Date;

public class ShowBookCopyDTO {
    private String isbn;

    private String author;

    private String name;

    private String uniqueBookMark;

    private String libraryName;

    private String imagePath;

    public ShowBookCopyDTO(String isbn, String author, String name, String uniqueBookMark, String libraryName, String imagePath) {
        this.isbn = isbn;
        this.author = author;
        this.name = name;
        this.uniqueBookMark = uniqueBookMark;
        this.libraryName = libraryName;
        this.imagePath = imagePath;
    }
}
