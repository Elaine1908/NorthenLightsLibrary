package com.example.lab2.dto.bookcopy;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * # 显示一本书及其副本的信息(zhj finished)接口中用到的dto
 */
@Getter
@Setter
@NoArgsConstructor
public class BookCopyDTO {

    private Long libraryID;
    private String libraryName;
    private String status;
    private String uniqueBookMark;
    private String borrower;

    public BookCopyDTO(Long libraryID, String libraryName, String status, String uniqueBookMark, String borrower) {
        this.libraryID = libraryID;
        this.libraryName = libraryName;
        this.status = status;
        this.uniqueBookMark = uniqueBookMark;
        this.borrower = borrower;
    }
}
