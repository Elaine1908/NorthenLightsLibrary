package com.example.lab2.service;

import com.example.lab2.dao.BookCopyRepository;
import com.example.lab2.dao.ReservationRepository;
import com.example.lab2.dao.UserConfigurationRepository;
import com.example.lab2.dao.UserRepository;
import com.example.lab2.entity.BookCopy;
import com.example.lab2.entity.Reservation;
import com.example.lab2.entity.User;
import com.example.lab2.exception.bookcopy.BookCopyNotAvailableException;
import com.example.lab2.exception.notfound.BookCopyNotFoundException;
import com.example.lab2.exception.notfound.UserNotFoundException;
import com.example.lab2.exception.reserve.ReservedByOtherException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ReserveServiceTest {

    @Autowired
    UserRepository userRepository;

    @Resource(name = "reserveService")
    ReserveService reserveService;

    @Autowired
    BookCopyRepository bookkCopyRepository;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    UserConfigurationRepository userConfigurationRepository;

    @Transactional
    @Test
    public void testReserveNonExistentBook() {
        User user = new User(
                "newUser",
                "password",
                "zhj@email.com",
                User.TEACHER,
                User.MAX_CREDIT
        );

        userRepository.save(user);

        assertThrows(BookCopyNotFoundException.class, () -> {
            reserveService.reserveBook("non_existent", "newUser");
        });

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
            assertEquals(e.getClass(), UserNotFoundException.class);
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
                User.TEACHER,
                User.MAX_CREDIT
        );


        bookkCopyRepository.save(bookCopy);
        userRepository.save(user);
        try {
            reserveService.reserveBook("uniqueBookMark", "newUser");
        } catch (Exception e) {
            assertTrue(e.getClass() == ReservedByOtherException.class || e.getClass() == BookCopyNotAvailableException.class);
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
                User.TEACHER,
                User.MAX_CREDIT
        );

        userRepository.save(user);
        bookkCopyRepository.save(bookCopy);

        reserveService.reserveBook("uniqueBookMark", "newUser");

        BookCopy bcFromDB = bookkCopyRepository.getBookCopyByUniqueBookMark("uniqueBookMark").get();
        assertEquals(bcFromDB.getStatus(), BookCopy.RESERVED);

        User userFromDb = userRepository.getUserByUsername("newUser");

        Reservation reservation = reservationRepository.getReservationByBookCopyID(bcFromDB.getBookCopyID()).get();

        long timeGap = reservation.getDeadline().getTime() - reservation.getReservationDate().getTime();

        long reserveTime = userConfigurationRepository.findUserConfigurationByRole(
                userFromDb.getRole()
        ).get().getMaxReserveTime();

        assertEquals(timeGap, reserveTime * 1000);
        assertEquals(reservation.getBookCopyID(), bcFromDB.getBookCopyID());
        assertEquals(reservation.getUserID().longValue(), userFromDb.getUser_id());

    }


}