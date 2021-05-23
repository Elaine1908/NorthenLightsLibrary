package com.example.lab2.request.fine;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PayFineResponseToTAServer {
    private String msg;

    public PayFineResponseToTAServer(String msg) {
        this.msg = msg;
    }
}
