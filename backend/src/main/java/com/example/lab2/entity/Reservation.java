package com.example.lab2.entity;


import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationID;

    @Column
    private Long userID;

    private Long bookCopyID;

    private Date reservationDate;

    private Date deadline;


    public Reservation(Long userID, Long bookCopyID, Date reservationDate) {
        this.userID = userID;
        this.bookCopyID = bookCopyID;
        this.reservationDate = reservationDate;
    }

    public Reservation(Long userID, Long bookCopyID, Date reservationDate, Date deadline) {
        this.userID = userID;
        this.bookCopyID = bookCopyID;
        this.reservationDate = reservationDate;
        this.deadline = deadline;
    }
}
