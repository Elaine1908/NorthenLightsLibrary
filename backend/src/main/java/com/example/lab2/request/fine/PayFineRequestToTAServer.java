package com.example.lab2.request.fine;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PayFineRequestToTAServer {
    private String invoke_id;
    private String uid;
    private double amount;

}
