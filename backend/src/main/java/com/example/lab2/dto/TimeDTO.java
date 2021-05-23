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

    public final static long DAY_SECS = 24 * 60 * 60;
    public final static long HOUR_SECS = 60 * 60;
    public final static long MIN_SECS = 60;

    public TimeDTO(long secs) {


        long secsLeft = secs;

        this.day = secsLeft / DAY_SECS;
        secsLeft -= DAY_SECS * this.day;

        this.hour = secsLeft / HOUR_SECS;
        secsLeft -= HOUR_SECS * this.hour;

        this.min = secsLeft / MIN_SECS;
        secsLeft -= this.min * MIN_SECS;

        this.sec = secsLeft;


    }

    public long getTotalSecs() {
        return DAY_SECS * day + HOUR_SECS * hour + MIN_SECS * min + sec;
    }

}
