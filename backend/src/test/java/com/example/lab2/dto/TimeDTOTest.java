package com.example.lab2.dto;

import org.junit.Test;

import java.sql.Time;

import static org.junit.jupiter.api.Assertions.*;

public class TimeDTOTest {

    @Test
    public void testTimeDTO() {
        TimeDTO timeDTO = new TimeDTO(24 * 3600 + 1);
        assertEquals(timeDTO.getDay(), 1);
        assertEquals(timeDTO.getHour(), 0);
        assertEquals(timeDTO.getMin(), 0);
        assertEquals(timeDTO.getSec(), 1);


    }

}