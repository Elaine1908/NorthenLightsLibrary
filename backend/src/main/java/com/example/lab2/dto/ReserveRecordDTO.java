package com.example.lab2.dto;

import com.example.lab2.entity.ReserveRecord;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * 用户预约记录的数据传输对象
 */
@Getter
@Setter
@NoArgsConstructor
public class ReserveRecordDTO {
    private Date reservationDate;

    private String uniqueBookMark;

    private String username;

    public ReserveRecordDTO(Date reservationDate,String uniqueBookMark,String username){
        this.username=username;
        this.reservationDate=reservationDate;
        this.uniqueBookMark=uniqueBookMark;
    }
}
