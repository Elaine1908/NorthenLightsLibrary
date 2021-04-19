package com.example.lab2.entity;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
public class BookCopy {

    //书籍的四种状态
    public static final String AVAILABLE = "available";
    public static final String BORROWED = "borrowed";
    public static final String RESERVED = "reserved";
    public static final String DAMAGED = "damaged";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookCopyID;

    @Column
    private String status;

    private String isbn;

    @Column(unique = true)
    private String uniqueBookMark;//ISBN-001,ISBN-002,ISBN-003......

    @Column(unique = false)
    private Long libraryID;

    private Date lastRentDate;

    private Date lastReturnDate;

    private Long adminID;

    public BookCopy(String status, String isbn, String uniqueBookMark, Long libraryID, Date lastRentDate, Date lastReturnDate, Long adminID) {
        this.status = status;
        this.isbn = isbn;
        this.uniqueBookMark = uniqueBookMark;
        this.libraryID = libraryID;
        this.lastRentDate = lastRentDate;
        this.lastReturnDate = lastReturnDate;
        this.adminID = adminID;
    }
}
