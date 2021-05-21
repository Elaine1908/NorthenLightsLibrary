package com.example.lab2.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class TimeDTO {

    private long day;
    private long hour;
    private long min;
    private long sec;

    public TimeDTO(long secs) {
        final long daySecs = 24 * 60 * 60;
        final long hourSecs = 60 * 60;
        final long minSecs = 60;

        long secsLeft = secs;

        this.day = secsLeft / daySecs;
        secsLeft -= daySecs * this.day;

        this.hour = secsLeft / hourSecs;
        secsLeft -= hourSecs * this.hour;

        this.min = secsLeft / minSecs;
        secsLeft -= this.min * minSecs;

        this.sec = secsLeft;


    }

}
