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

    private long adminID;

    public ReserveRecord(long userID,Date time,String uniqueBookMark,long adminID,long libraryID){
        this.adminID = adminID;
        this.userID=userID;
        this.libraryID=libraryID;
        this.time=time;
        this.uniqueBookMark=uniqueBookMark;
    }
}
