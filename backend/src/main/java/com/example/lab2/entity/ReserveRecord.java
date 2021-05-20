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
public class ReserveRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reserveRecordID;

    @Column
    private long userID;

    private Date time;

    private String uniqueBookMark;

    private long libraryID;

    private String admin;

    public ReserveRecord(long userID,Date time,String uniqueBookMark,String admin,long libraryID){
        this.admin = admin;
        this.userID=userID;
        this.libraryID=libraryID;
        this.time=time;
        this.uniqueBookMark=uniqueBookMark;
    }
}
