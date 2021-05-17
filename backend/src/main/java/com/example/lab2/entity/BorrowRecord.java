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
public class BorrowRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long borrowRecordID;

    @Column
    private String username;

    private String time;

    private String uniqueBookMark;

    private String libraryName;

    private String adminName;

    public BorrowRecord(String username,String time,String uniqueBookMark,String adminName,String libraryName){
        this.adminName = adminName;
        this.username=username;
        this.libraryName=libraryName;
        this.time=time;
        this.uniqueBookMark=uniqueBookMark;
    }
}
