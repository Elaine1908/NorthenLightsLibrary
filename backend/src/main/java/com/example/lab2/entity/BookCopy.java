package com.example.lab2.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@ToString
public class BookCopy {

    //书籍的四种状态
    public static final String AVAILABLE = "可用（在架上）";
    public static final String BORROWED = "已被借走";
    public static final String RESERVED = "已被预定";
    public static final String DAMAGED = "已损坏";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long bookCopyID;

    @Column
    private String status;

    @JsonIgnore
    private String isbn;

    @Column(unique = true)
    private String uniqueBookMark;//ISBN-001,ISBN-002,ISBN-003......

    @Column(unique = false)
    @JsonIgnore
    private Long libraryID;

    private Date lastRentDate;

    private Date lastReturnDate;

    private Date lastReservationDate;

    @JsonIgnore
    private Long adminID;

    //这两个属性是连表查询的时候为了方便加的
    @Transient
    private String borrower;

    @Transient
    private String libraryName;

    public BookCopy(String status, String isbn, String uniqueBookMark, Long libraryID, Date lastRentDate, Date lastReturnDate, Long adminID) {
        this.status = status;
        this.isbn = isbn;
        this.uniqueBookMark = uniqueBookMark;
        this.libraryID = libraryID;
        this.lastRentDate = lastRentDate;
        this.lastReturnDate = lastReturnDate;
        this.adminID = adminID;
    }

    public BookCopy(Long bookCopyID, String status, String uniqueBookMark, String borrower, String libraryName, Long
            libraryID) {
        this.bookCopyID = bookCopyID;
        this.status = status;
        this.uniqueBookMark = uniqueBookMark;
        this.borrower = borrower;
        this.libraryName = libraryName;
        this.libraryID = libraryID;
    }

    /**
     * 返回这本图书目前是否可用
     *
     * @return
     */
    public boolean isAvailable() {
        return this.status.equals(BookCopy.AVAILABLE);
    }
}
