package com.example.lab2.dto.record;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 用户还书记录的数据传输对象
 */
@Getter
@Setter
public class ReturnRecordDTO extends RecordAboutBookCopyDTO {

    public ReturnRecordDTO() {
        this.type = RecordAboutBookCopyDTO.RETURN_RECORD;
    }

    public ReturnRecordDTO(Date time, String uniqueBookMark, String username, String admin, String library) {
        this();
        this.admin = admin;
        this.library = library;
        this.time = time;
        this.uniqueBookMark = uniqueBookMark;
        this.username = username;
    }
}
