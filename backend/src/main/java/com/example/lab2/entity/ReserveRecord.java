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


    public ReserveRecord(long userID,Date time,String uniqueBookMark){
        this.userID=userID;
        this.time=time;
        this.uniqueBookMark=uniqueBookMark;
    }
}
