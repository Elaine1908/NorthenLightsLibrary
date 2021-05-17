package com.example.lab2.entity;

import lombok.*;

import javax.persistence.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ReturnRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long returnRecordID;

    @Column
    private String username;

    private String time;

    private String uniqueBookMark;

    private String libraryName;

    private String adminName;

    public ReturnRecord(String username,String time,String uniqueBookMark,String adminName,String libraryName){
        this.adminName = adminName;
        this.username=username;
        this.libraryName=libraryName;
        this.time=time;
        this.uniqueBookMark=uniqueBookMark;
    }
}
