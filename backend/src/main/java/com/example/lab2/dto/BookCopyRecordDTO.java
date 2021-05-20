package com.example.lab2.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.awt.print.Book;
import java.util.Date;

/**
 * 副本记录的数据传输对象
 */
@Getter
@Setter
@NoArgsConstructor
public class BookCopyRecordDTO implements Comparable<BookCopyRecordDTO>{
    public static final String reserveRecord = "预约";
    public static final String borrowRecord = "借阅";
    public static final String returnRecord = "归还";
    private Date time;
    private String uniqueBookMark;
    private String username;
    private String admin;
    private String library;
    private String type;

    public BookCopyRecordDTO(Date time,String uniqueBookMark,String username,String admin,String library,String type){
        this.time=time;
        this.admin=admin;
        this.library=library;
        this.uniqueBookMark=uniqueBookMark;
        this.type=type;
        this.username=username;
    }

    @Override
    public int compareTo(BookCopyRecordDTO o) {
        int time = (int)(this.getTime().getTime() - o.getTime().getTime());
        return time;
    }
}
