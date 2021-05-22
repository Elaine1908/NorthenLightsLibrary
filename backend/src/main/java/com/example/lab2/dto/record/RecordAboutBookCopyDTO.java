package com.example.lab2.dto.record;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 是和“书的副本”有关的Record的DTO类。BorrowRecordDTO,ReserveRecordDTO,ReturnRecordDTO都继承自此类
 */
@Getter
@Setter
public abstract class RecordAboutBookCopyDTO implements Comparable<RecordAboutBookCopyDTO> {
    public static final String RESERVE_RECORD = "预约";
    public static final String BORROW_RECORD = "借阅";
    public static final String RETURN_RECORD = "归还";
    public static final String UNAVAILABLE = "不适用";
    protected Date time;
    protected String uniqueBookMark;
    protected String username;
    protected String admin;
    protected String library;
    protected String type;

    public RecordAboutBookCopyDTO() {
    }

    public RecordAboutBookCopyDTO(Date time, String uniqueBookMark, String username, String admin, String library, String type) {
        this.time = time;
        this.admin = admin;
        this.library = library;
        this.uniqueBookMark = uniqueBookMark;
        this.type = type;
        this.username = username;
    }


    @Override
    public int compareTo(RecordAboutBookCopyDTO o) {
        return this.time.compareTo(o.time);
    }
}
