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
public class ReturnRecord {

    private static String OK = "ok";
    private static String DAMAGED = "damaged";
    private static String LOST = "lost";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long returnRecordID;

    @Column
    private long userID;

    private Date time;

    private String uniqueBookMark;

    private long libraryID;

    private String admin;

    private String status;

    public ReturnRecord(long userID, Date time, String uniqueBookMark, String admin, long libraryID) {
        this.admin = admin;
        this.userID = userID;
        this.libraryID = libraryID;
        this.time = time;
        this.uniqueBookMark = uniqueBookMark;
    }
}
