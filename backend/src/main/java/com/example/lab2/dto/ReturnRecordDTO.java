package com.example.lab2.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * 用户还书记录的数据传输对象
 */
@Getter
@Setter
@NoArgsConstructor
public class ReturnRecordDTO {
    private Date time;
    private String uniqueBookMark;
    private String username;
    private String admin;
    private String library;

    public ReturnRecordDTO(Date time,String uniqueBookMark,String username,String admin,String library){
        this.admin=admin;
        this.library=library;
        this.time=time;
        this.uniqueBookMark=uniqueBookMark;
        this.username=username;
    }
}
