package com.example.lab2.dao;

import com.example.lab2.entity.Reservation;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;


@SpringBootTest
@RunWith(SpringRunner.class)
public class ReservationRepositoryTest {

    @Autowired
    ReservationRepository reservationRepository;

    @Test
    @Transactional
    public void getReservationCountByUserID() {
        for (int i = 0; i < 10; i++) {
            reservationRepository.save(new Reservation(
                    (long) 1, (long) i
            ));
        }

        assertEquals(reservationRepository.getReservationCountByUserID((long)1).longValue(),(long) 10);


    }

}