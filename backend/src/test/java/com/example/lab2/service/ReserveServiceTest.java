package com.example.lab2.service;

import com.example.lab2.dao.BookkCopyRepository;
import com.example.lab2.dao.ReservationRepository;
import com.example.lab2.dao.UserRepository;
import com.example.lab2.entity.BookCopy;
import com.example.lab2.entity.Reservation;
import com.example.lab2.entity.User;
import com.example.lab2.exception.ReserveException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.xml.ws.soap.Addressing;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ReserveServiceTest {

    @Autowired
    UserRepository userRepository;

    @Resource(name = "reserveService")
    ReserveService reserveService;

    @Autowired
    BookkCopyRepository bookkCopyRepository;

    @Autowired
    ReservationRepository reservationRepository;

    @Transactional
    @Test
    public void testReserveNonExistentBook() {
        User user = new User(
                "newUser",
                "password",
                "zhj@email.com",
                User.STUDENT,
                User.MAX_CREDIT
        );

        userRepository.save(user);

        try {
            reserveService.reserveBook("non_existent_book", "newUser");
        } catch (Exception e) {
            assertEquals(e.getClass(), ReserveException.class);
            assertEquals(e.getMessage(), "该副本不存在！");
        }

    }

    @Transactional
    @Test
    public void testReserveNonExistentUser() {
        BookCopy bookCopy = new BookCopy(
                BookCopy.AVAILABLE,
                "isbn",
                "uniqueBookMark",
                (long) 0,
                null,
                null,
                (long) 0
        );

        bookkCopyRepository.save(bookCopy);

        try {
            reserveService.reserveBook("uniqueBookMark", "non_existent_user");
        } catch (Exception e) {
            assertEquals(e.getClass(), ReserveException.class);
            assertEquals(e.getMessage(), "用户不存在！");
        }

    }

    //测试预约有人已经预约了的图书
    @Transactional
    @Test
    public void testReserveReservedBook() {
        BookCopy bookCopy = new BookCopy(
                BookCopy.RESERVED,
                "isbn",
                "uniqueBookMark",
                (long) 0,
                null,
                null,
                (long) 0
        );
        User user = new User(
                "newUser",
                "password",
                "zhj@email.com",
                User.STUDENT,
                User.MAX_CREDIT
        );


        bookkCopyRepository.save(bookCopy);
        userRepository.save(user);
        try {
            reserveService.reserveBook("uniqueBookMark", "newUser");
        } catch (Exception e) {
            assertEquals(e.getClass(), ReserveException.class);
        }

    }


    @Transactional
    @Test
    public void testReserveBookNormal() {
        BookCopy bookCopy = new BookCopy(
                BookCopy.AVAILABLE,
                "isbn",
                "uniqueBookMark",
                (long) 0,
                null,
                null,
                (long) 0
        );
        User user = new User(
                "newUser",
                "password",
                "zhj@email.com",
                User.STUDENT,
                User.MAX_CREDIT
        );

        userRepository.save(user);
        bookkCopyRepository.save(bookCopy);

        reserveService.reserveBook("uniqueBookMark", "newUser");

        BookCopy bcFromDB = bookkCopyRepository.getBookCopyByUniqueBookMark("uniqueBookMark").get();
        assertEquals(bcFromDB.getStatus(), BookCopy.RESERVED);

        User userFromDb = userRepository.getUserByUsername("newUser");

        Reservation reservation = reservationRepository.getReservationByBookCopyID(bcFromDB.getBookCopyID()).get();

        assertEquals(reservation.getBookCopyID(), bcFromDB.getBookCopyID());
        assertEquals(reservation.getUserID().longValue(), userFromDb.getUser_id());

    }


}