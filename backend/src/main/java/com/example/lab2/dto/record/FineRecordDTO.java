package com.example.lab2.dto.record;

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

    private Long fineID;

    private Date time;

    private String reason;

    private String username;

    private String status;

    private Long money;

    public FineRecordDTO(Date time, String reason, String username, String status, long money) {
        this.money = money;
        this.reason = reason;
        this.status = status;
        this.time = time;
        this.username = username;
    }

    public FineRecordDTO(Long fineID, Date time, String reason, String username, String status, Long money) {
        this.fineID = fineID;
        this.time = time;
        this.reason = reason;
        this.username = username;
        this.status = status;
        this.money = money;
    }
}
