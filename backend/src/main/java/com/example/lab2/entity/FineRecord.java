package com.example.lab2.entity;

import lombok.*;

import javax.persistence.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class FineRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fineRecordID;

    @Column
    private String username;

    private String time;

    private String money;

    private String status;

    public FineRecord(String username,String time,String money,String status){
        this.status=status;
        this.username=username;
        this.time=time;
        this.money=money;
    }
}
