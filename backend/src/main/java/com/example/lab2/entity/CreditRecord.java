package com.example.lab2.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreditRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long creditRecordID;

    private long userID;

    private long amount;

    private String description;

    private Date time;

    public CreditRecord(long userID, long amount, String description, Date time) {
        this.userID = userID;
        this.amount = amount;
        this.description = description;
        this.time = time;
    }
}
