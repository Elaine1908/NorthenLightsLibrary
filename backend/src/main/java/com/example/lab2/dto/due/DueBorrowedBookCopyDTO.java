package com.example.lab2.dto.due;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor

public class DueBorrowedBookCopyDTO implements DueDTO {

    private long borrowID;

    private Date borrowDate;

    private String isbn;

    private String author;

    private String name;

    private String uniqueBookMark;

    private String imagePath;

    private Date deadline;

    private String username;

    private String email;

    public DueBorrowedBookCopyDTO(long borrowID, Date borrowDate, String isbn, String author, String name, String uniqueBookMark, String imagePath, Date deadline, String username, String email) {
        this.borrowID = borrowID;
        this.borrowDate = borrowDate;
        this.isbn = isbn;
        this.author = author;
        this.name = name;
        this.uniqueBookMark = uniqueBookMark;
        this.imagePath = imagePath;
        this.deadline = deadline;
        this.username = username;
        this.email = email;
    }

    @Override
    public String getDueMessage() {
        return String.format("你借阅的图书%s%s已于%s到期，请尽快归还\n", name, uniqueBookMark, deadline.toString());
    }

    @Override
    public String getHeadMessage() {
        return String.format("用户%s，你好\n", username);
    }
}
