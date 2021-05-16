package com.example.lab2.entity;


import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Borrow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long borrowID;

    @Column
    private Long userID;
    private String uniqueBookMark;
    private Date borrowDate;
    private Date deadline;

    public Borrow(Long userID, String uniqueBookMark, Date borrowDate) {
        this.userID = userID;
        this.uniqueBookMark = uniqueBookMark;
        this.borrowDate = borrowDate;
    }
}
