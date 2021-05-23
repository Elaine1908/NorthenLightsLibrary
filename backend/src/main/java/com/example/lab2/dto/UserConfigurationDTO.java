package com.example.lab2.dto;


import com.example.lab2.entity.UserConfiguration;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserConfigurationDTO {

    String role;

    long maxBookBorrow;

    TimeDTO maxBorrowTime;
    TimeDTO maxReserveTime;

    public UserConfigurationDTO(String role, long maxBookBorrow, TimeDTO maxBorrowTime, TimeDTO maxReserveTimeDTO) {
        this.role = role;
        this.maxBookBorrow = maxBookBorrow;
        this.maxBorrowTime = maxBorrowTime;
        this.maxReserveTime = maxReserveTimeDTO;
    }

    public UserConfigurationDTO(UserConfiguration userConfiguration) {
        this.role = userConfiguration.getRole();
        this.maxBookBorrow = userConfiguration.getMaxBookBorrow();
        this.maxBorrowTime = new TimeDTO(userConfiguration.getMaxBorrowTime());
        this.maxReserveTime = new TimeDTO(userConfiguration.getMaxReserveTime());

    }
}
