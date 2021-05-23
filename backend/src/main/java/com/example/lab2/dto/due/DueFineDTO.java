package com.example.lab2.dto.due;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class DueFineDTO implements DueDTO {

    long fineID;
    long money;
    String reason;
    Date date;
    String username;
    String email;

    public DueFineDTO(long fineID, long money, String reason, Date date, String username, String email) {
        this.fineID = fineID;
        this.money = money;
        this.reason = reason;
        this.date = date;
        this.username = username;
        this.email = email;
    }

    @Override
    public String getDueMessage() {
        return String.format("你由于%s遭受的罚款%.2f元还未支付，请尽快支付\n", reason, money / 100.0);
    }

    @Override
    public String getHeadMessage() {
        return String.format("用户%s，你好\n", username);
    }
}
