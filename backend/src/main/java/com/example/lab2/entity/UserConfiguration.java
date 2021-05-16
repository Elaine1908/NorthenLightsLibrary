package com.example.lab2.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class UserConfiguration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userConfigurationID;

    @Column(unique = true)
    String role;

    long maxBookBorrow;
    long maxBorrowTime;
    long maxReserveTime;

    public UserConfiguration(String role, long maxBookBorrow, long maxBorrowTime, long maxReserveTime) {
        this.role = role;
        this.maxBookBorrow = maxBookBorrow;
        this.maxBorrowTime = maxBorrowTime;
        this.maxReserveTime = maxReserveTime;
    }
}
