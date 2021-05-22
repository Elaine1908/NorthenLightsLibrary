package com.example.lab2.dto.record;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 用户预约记录的数据传输对象
 */
@Getter
@Setter
public class ReserveRecordDTO extends RecordAboutBookCopyDTO {

    public ReserveRecordDTO() {

        //由于预约记录不需要显示管理员姓名和图书馆，因此把它俩设置成“不可用”
        //其实这里的继承设计是有一点奇怪的。但是由于要向外界展示统一的接口，只能把这两个不适合的属性也写在抽象基类里
        this.admin = RecordAboutBookCopyDTO.UNAVAILABLE;
        this.library = RecordAboutBookCopyDTO.UNAVAILABLE;
        this.type = RecordAboutBookCopyDTO.RESERVE_RECORD;
    }


    public ReserveRecordDTO(Date reservationDate, String uniqueBookMark, String username) {
        this();
        this.username = username;
        this.time = reservationDate;
        this.uniqueBookMark = uniqueBookMark;
    }
}
