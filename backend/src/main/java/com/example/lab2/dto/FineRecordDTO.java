package com.example.lab2.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * 用户罚款记录的数据传输对象
 */
@Getter
@Setter
@NoArgsConstructor
public class FineRecordDTO {
    private Date time;

    private String reason;

    private String username;

    private String status;

    private long money;

    public FineRecordDTO(Date time,String reason,String username,String status,long money){
        this.money=money;
        this.reason=reason;
        this.status=status;
        this.time=time;
        this.username=username;
    }
}
