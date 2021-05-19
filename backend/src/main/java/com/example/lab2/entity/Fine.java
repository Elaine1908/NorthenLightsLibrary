package com.example.lab2.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Fine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long fineID;
    private long money;

    @JsonIgnore
    private long userID;
    private String reason;
    private Date date;

    public Fine(long fineID, long money, long userID, String reason, Date date) {
        this.fineID = fineID;
        this.money = money;
        this.userID = userID;
        this.reason = reason;
        this.date = date;
    }

    public Fine(long money, long userID, String reason, Date date) {
        this.money = money;
        this.userID = userID;
        this.reason = reason;
        this.date = date;
    }
}
