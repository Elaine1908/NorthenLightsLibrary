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
public class FineRecord {
    public static final String UNPAID = "未支付";
    public static final String PAID = "已支付";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fineRecordID;

    @Column
    private long userID;

    private Date time;

    private long money;

    private String status;

    private String reason;

    public FineRecord(long userID,Date time,long money,String status,String reason){
        this.status=status;
        this.userID=userID;
        this.time=time;
        this.money=money;
        this.reason=reason;
    }
}
