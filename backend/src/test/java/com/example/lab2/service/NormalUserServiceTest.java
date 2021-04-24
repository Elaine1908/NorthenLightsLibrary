package com.example.lab2.service;

import com.example.lab2.dao.BorrowRepository;
import com.example.lab2.dao.ReservationRepository;
import com.example.lab2.dao.UserRepository;
import com.example.lab2.entity.Borrow;
import com.example.lab2.entity.Reservation;
import com.example.lab2.entity.User;
import com.example.lab2.response.UserInfoResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.security.SecureRandom;
import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class NormalUserServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    BorrowRepository borrowRepository;

    @Resource(name = "normalUserService")
    NormalUserService normalUserService;

    @Test
    @Transactional
    public void testGetUserInfo() {

        User user = new User(
                "newUser",
                "password",
                "zhj@email.com",
                User.STUDENT,
                User.MAX_CREDIT
        );
        userRepository.save(user);

        User userFromDB = userRepository.getUserByUsername("newUser");

        SecureRandom secureRandom = new SecureRandom();

        int reservationNumber = 3;
        int borrowNumber = 4;

        for (int i = 0; i < reservationNumber; i++) {
            Reservation
                    reservation = new Reservation(
                    userFromDB.getUser_id(),
                    secureRandom.nextLong(),
                    new Date()
            );
            reservationRepository.save(reservation);

        }

        for (int i = 0; i < borrowNumber; i++) {
            Borrow borrow = new
                    Borrow(
                    user.getUser_id(),
                    UUID.randomUUID().toString(),
                    new Date()
            );
            borrowRepository.save(borrow);


        }

        UserInfoResponse userInfoResponse = normalUserService.userInfo("newUser");

        assertEquals(userInfoResponse.getBorrowedBooks().size(), borrowNumber);
        assertEquals(userInfoResponse.getReservedBooks().size(), reservationNumber);
    }

}