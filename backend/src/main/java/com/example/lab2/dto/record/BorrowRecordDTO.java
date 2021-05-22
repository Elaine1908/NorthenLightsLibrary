package com.example.lab2.dto.record;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 用户借阅记录的数据传输对象
 */
@Getter
@Setter
public class BorrowRecordDTO extends RecordAboutBookCopyDTO {

    /**
     * 无参构造函数。由于此类的任何构造函数都会隐式调用它，因此用它来初始化type属性
     */
    public BorrowRecordDTO() {
        this.type = RecordAboutBookCopyDTO.BORROW_RECORD;
    }

    public BorrowRecordDTO(Date time, String uniqueBookMark, String username, String admin, String library) {
        this();
        this.admin = admin;
        this.library = library;
        this.time = time;
        this.uniqueBookMark = uniqueBookMark;
        this.username = username;
    }
}
