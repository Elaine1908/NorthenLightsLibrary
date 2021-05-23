package com.example.lab2.dto.due;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class DueReservedBookCopyDTO implements DueDTO {

    private long reservationID;

    private Date reservationDate;

    private String isbn;

    private String author;

    private String name;

    private String uniqueBookMark;

    private String libraryName;

    private String imagePath;

    private Date deadline;

    private String username;

    private String email;

    public DueReservedBookCopyDTO(long reservationID, Date reservationDate, String isbn, String author, String name, String uniqueBookMark, String libraryName, String imagePath, Date deadline, String username, String email) {
        this.reservationID = reservationID;
        this.reservationDate = reservationDate;
        this.isbn = isbn;
        this.author = author;
        this.name = name;
        this.uniqueBookMark = uniqueBookMark;
        this.libraryName = libraryName;
        this.imagePath = imagePath;
        this.deadline = deadline;
        this.username = username;
        this.email = email;
    }

    @Override
    public String getDueMessage() {
        return String.format("你预约的图书%s%s已于%s到期，系统已经取消此预约%n", name, uniqueBookMark, deadline.toString());
    }

    @Override
    public String getHeadMessage() {
        return String.format("用户%s，你好%n", username);
    }
}
