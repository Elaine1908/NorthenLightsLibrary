package com.example.lab2.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * 用户已经预约的书的数据传输对象
 */
@Getter
@Setter
@NoArgsConstructor
public class ReservedBookCopyDTO {

    private Date reservationDate;

    private String isbn;

    private String author;

    private String name;

    private String uniqueBookMark;

    private String libraryName;

    public ReservedBookCopyDTO(Date reservationDate, String isbn, String author, String name, String uniqueBookMark, String libraryName) {
        this.reservationDate = reservationDate;
        this.isbn = isbn;
        this.author = author;
        this.name = name;
        this.uniqueBookMark = uniqueBookMark;
        this.libraryName = libraryName;
    }
}
