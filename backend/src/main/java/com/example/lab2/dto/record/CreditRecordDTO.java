package com.example.lab2.dto.record;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class CreditRecordDTO {

    private long amount;

    private String description;

    private Date time;

    public CreditRecordDTO(long amount, String description, Date time) {
        this.amount = amount;
        this.description = description;
        this.time = time;
    }
}
