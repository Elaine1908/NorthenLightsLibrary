package com.example.lab2.service;

import com.example.lab2.dao.BookkCopyRepository;
import com.example.lab2.dao.BorrowRepository;
import com.example.lab2.dao.ReservationRepository;
import com.example.lab2.dao.UserRepository;
import com.example.lab2.entity.BookCopy;
import com.example.lab2.entity.Borrow;
import com.example.lab2.entity.Reservation;
import com.example.lab2.entity.User;
import com.example.lab2.exception.bookcopy.BookCopyIsBorrowedException;
import com.example.lab2.exception.bookcopy.BookCopyNotHereException;
import com.example.lab2.exception.bookcopy.BookCopyReservedException;
import com.example.lab2.exception.notfound.BookCopyNotFoundException;
import com.example.lab2.exception.notfound.UserNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.Date;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class BorrowServiceTest {

    @Resource(name = "borrowService")
    BorrowService borrowService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BookkCopyRepository bookkCopyRepository;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    BorrowRepository borrowRepository;

    @Test
    @Transactional
    public void testLendNonExistentBookToUser() {

        User user = new User(
                "newUser",
                "password",
                "zhj@email.com",
                User.STUDENT,
                User.MAX_CREDIT
        );

        userRepository.save(user);

        assertThrows(BookCopyNotFoundException.class, () -> {
            borrowService.lendBookToUser("non_existent_book", "newUser", (long) 3);
        });

    }

    @Transactional
    @Test
    public void testLendBookToNonexistentUser() {
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

        assertThrows(UserNotFoundException.class, () -> {
            borrowService.lendBookToUser("uniqueBookMark", "non_existent_user", (long) 3);

        });

    }

    @Test
    @Transactional
    public void testLentBookNotInThisLibraryToUser() {
        BookCopy bookCopy = new BookCopy(
                BookCopy.AVAILABLE,
                "isbn",
                "uniqueBookMark",
                (long) 1,
                null,
                null,
                (long) 1
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
        assertThrows(BookCopyNotHereException.class, () -> {
            borrowService.lendBookToUser("uniqueBookMark", "newUser", (long) 0);
        });


    }

    @Test
    @Transactional
    public void testLendReservedBookToUser() {
        BookCopy bookCopy = new BookCopy(
                BookCopy.AVAILABLE,
                "isbn",
                "uniqueBookMark",
                (long) 1,
                null,
                null,
                (long) 1
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

        BookCopy bookCopyFromDB = bookkCopyRepository.getBookCopyByUniqueBookMark("uniqueBookMark").get();

        Reservation reservation = new Reservation(
                (long) 345, bookCopyFromDB.getBookCopyID()
        );

        reservationRepository.save(reservation);

        assertThrows(BookCopyReservedException.class, () -> {
            borrowService.lendBookToUser("uniqueBookMark", "newUser", (long) 1);
        });

    }

    @Transactional
    @Test
    public void testLendBorrowedBookToUser() {
        BookCopy bookCopy = new BookCopy(
                BookCopy.AVAILABLE,
                "isbn",
                "uniqueBookMark",
                (long) 1,
                null,
                null,
                (long) 1
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

        Borrow borrow = new Borrow(
                (long) 345, "uniqueBookMark", new Date()
        );
        borrowRepository.save(borrow);

        assertThrows(BookCopyIsBorrowedException.class, () -> {
            borrowService.lendBookToUser("uniqueBookMark", "newUser", (long) 1);
        });


    }

    @Test
    @Transactional
    public void testLendBookToUserNormal() {
        BookCopy bookCopy = new BookCopy(
                BookCopy.AVAILABLE,
                "isbn",
                "uniqueBookMark",
                (long) 1,
                null,
                null,
                (long) 1
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

        borrowService.lendBookToUser("uniqueBookMark", "newUser", (long) 1);

        Borrow borrowFromDb = borrowRepository.getBorrowByUniqueBookMark("uniqueBookMark").get();
        User userFromDb = userRepository.getUserByUsername("newUser");
        BookCopy bookCopyFromDb = bookkCopyRepository.getBookCopyByUniqueBookMark("uniqueBookMark").get();

        assertEquals(borrowFromDb.getUserID().longValue(), userFromDb.getUser_id());
        assertEquals(borrowFromDb.getUniqueBookMark(), "uniqueBookMark");
        assertEquals(bookCopyFromDb.getStatus(), BookCopy.BORROWED);
        assertNotNull(bookCopyFromDb.getLastRentDate());


    }


}