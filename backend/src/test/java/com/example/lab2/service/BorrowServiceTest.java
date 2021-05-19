package com.example.lab2.service;

import com.example.lab2.dao.*;
import com.example.lab2.entity.*;
import com.example.lab2.exception.bookcopy.BookCopyIsBorrowedException;
import com.example.lab2.exception.bookcopy.BookCopyNotHereException;
import com.example.lab2.exception.bookcopy.BookCopyReservedException;
import com.example.lab2.exception.notfound.BookCopyNotFoundException;
import com.example.lab2.exception.notfound.UserNotFoundException;
import com.example.lab2.exception.reserve.NotReservedException;
import com.example.lab2.exception.reserve.ReservedByOtherException;
//import jdk.nashorn.internal.runtime.options.Option;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.security.SecureRandom;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class BorrowServiceTest {

    @Resource(name = "borrowService")
    BorrowService borrowService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BookCopyRepository bookkCopyRepository;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    BorrowRepository borrowRepository;

    @Autowired
    LibraryRepository libraryRepository;

    @Autowired
    UserConfigurationRepository userConfigurationRepository;

    @Test
    @Transactional
    public void testLendNonExistentBookToUser() {

        User user = new User(
                "newUser",
                "password",
                "zhj@email.com",
                User.POSTGRADUATE,
                User.MAX_CREDIT
        );

        userRepository.save(user);

        assertThrows(BookCopyNotFoundException.class, () -> {
            borrowService.lendBookToUser("non_existent_book", "newUser", (long) 3, "admin");
        });

    }

    @Transactional
    @Test
    public void testLendBookToNonexistentUser() {
        BookCopy bookCopy = new BookCopy(
                BookCopy.AVAILABLE,
                "1111111111",
                "1111111111-1",
                (long) 0,
                null,
                null,
                (long) 0
        );

        bookkCopyRepository.save(bookCopy);

        assertThrows(UserNotFoundException.class, () -> {
            borrowService.lendBookToUser("1111111111-1", "non_existent_user", (long) 3, "admin");

        });

    }

    @Test
    @Transactional
    public void testLentBookNotInThisLibraryToUser() {
        BookCopy bookCopy = new BookCopy(
                BookCopy.AVAILABLE,
                "1111111111",
                "1111111111-1",
                (long) 1,
                null,
                null,
                (long) 1
        );
        User user = new User(
                "newUser",
                "password",
                "zhj@email.com",
                User.POSTGRADUATE,
                User.MAX_CREDIT
        );
        userRepository.save(user);
        bookkCopyRepository.save(bookCopy);
        assertThrows(BookCopyNotHereException.class, () -> {
            borrowService.lendBookToUser("1111111111-1", "newUser", (long) 0, "admin");
        });


    }

    @Test
    @Transactional
    public void testLendReservedBookToUser() {
        BookCopy bookCopy = new BookCopy(
                BookCopy.AVAILABLE,
                "1111111111",
                "1111111111-1",
                (long) 1,
                null,
                null,
                (long) 1
        );
        User user = new User(
                "newUser",
                "password",
                "zhj@email.com",
                User.POSTGRADUATE,
                User.MAX_CREDIT
        );
        userRepository.save(user);
        bookkCopyRepository.save(bookCopy);

        BookCopy bookCopyFromDB = bookkCopyRepository.getBookCopyByUniqueBookMark("1111111111-1").get();

        Reservation reservation = new Reservation(
                (long) 345, bookCopyFromDB.getBookCopyID(), new Date()
        );

        reservationRepository.save(reservation);

        assertThrows(BookCopyReservedException.class, () -> {
            borrowService.lendBookToUser("1111111111-1", "newUser", (long) 1, "admin");
        });

    }

    @Transactional
    @Test
    public void testLendBorrowedBookToUser() {
        BookCopy bookCopy = new BookCopy(
                BookCopy.AVAILABLE,
                "1111111111",
                "1111111111-1",
                (long) 1,
                null,
                null,
                (long) 1
        );
        User user = new User(
                "newUser",
                "password",
                "zhj@email.com",
                User.POSTGRADUATE,
                User.MAX_CREDIT
        );
        userRepository.save(user);
        bookkCopyRepository.save(bookCopy);

        Borrow borrow = new Borrow(
                (long) 345, "1111111111-1", new Date(),new Date()
        );
        borrowRepository.save(borrow);

        assertThrows(BookCopyIsBorrowedException.class, () -> {
            borrowService.lendBookToUser("1111111111-1", "newUser", (long) 1, "admin");
        });


    }

    @Test
    @Transactional
    public void testLendBookToUserNormal() {
        BookCopy bookCopy = new BookCopy(
                BookCopy.AVAILABLE,
                "1111111111",
                "1111111111-1",
                (long) 1,
                null,
                null,
                (long) 1
        );
        User user = new User(
                "newUser",
                "password",
                "zhj@email.com",
                User.POSTGRADUATE,
                User.MAX_CREDIT
        );
        userRepository.save(user);
        bookkCopyRepository.save(bookCopy);

        borrowService.lendBookToUser("1111111111-1", "newUser", (long) 1, "admin");

        Borrow borrowFromDb = borrowRepository.getBorrowByUniqueBookMark("1111111111-1").get();
        User userFromDb = userRepository.getUserByUsername("newUser");
        BookCopy bookCopyFromDb = bookkCopyRepository.getBookCopyByUniqueBookMark("1111111111-1").get();
        Optional<UserConfiguration> userConfigurationOptional = userConfigurationRepository.findUserConfigurationByRole(user.getRole());
        long timegap = borrowFromDb.getDeadline().getTime() - borrowFromDb.getBorrowDate().getTime();

        assertEquals(timegap,1000*userConfigurationOptional.get().getMaxBorrowTime());
        assertEquals(borrowFromDb.getUserID().longValue(), userFromDb.getUser_id());
        assertEquals(borrowFromDb.getUniqueBookMark(), "1111111111-1");
        assertEquals(bookCopyFromDb.getStatus(), BookCopy.BORROWED);
        assertNotNull(bookCopyFromDb.getLastRentDate());


    }


    @Transactional
    @Test
    public void lendOnlyOneReservedBookToUser_BookNonExistent() {
        User user = new User(
                "newUser",
                "password",
                "zhj@email.com",
                User.POSTGRADUATE,
                User.MAX_CREDIT
        );
        userRepository.save(user);

        User userFromDb = userRepository.getUserByUsername("newUser");

        List<Library> libraries = libraryRepository.findAll();

        assertThrows(BookCopyNotFoundException.class, () -> {
            borrowService.lendOnlyOneReservedBookToUser(
                    "non_existent",
                    (long) 0,
                    libraries,

                    userFromDb,
                    "admin"
            );
        });

    }

    @Transactional
    @Test
    public void lendOnlyOneReservedBookToUser_BookNotReserved() {
        BookCopy bookCopy = new BookCopy(
                BookCopy.AVAILABLE,
                "1111111111",
                "1111111111-1",
                (long) 1,
                null,
                null,
                (long) 1
        );
        User user = new User(
                "newUser",
                "password",
                "zhj@email.com",
                User.POSTGRADUATE,
                User.MAX_CREDIT
        );
        userRepository.save(user);
        bookkCopyRepository.save(bookCopy);

        User userFromDb = userRepository.getUserByUsername("newUser");

        List<Library> libraries = libraryRepository.findAll();

        assertThrows(NotReservedException.class, () -> {
            borrowService.lendOnlyOneReservedBookToUser(
                    "1111111111-1",
                    (long) 0,
                    libraries,

                    userFromDb,
                    "admin"
            );
        });
    }

    @Transactional
    @Test
    public void lendOnlyOneReservedBookToUser_BookReservedByOther() {
        BookCopy bookCopy = new BookCopy(
                BookCopy.AVAILABLE,
                "1111111111",
                "1111111111-1",
                (long) 1,
                null,
                null,
                (long) 1
        );
        User user = new User(
                "newUser",
                "password",
                "zhj@email.com",
                User.POSTGRADUATE,
                User.MAX_CREDIT
        );


        userRepository.save(user);
        bookkCopyRepository.save(bookCopy);

        User userFromDb = userRepository.getUserByUsername("newUser");
        BookCopy bookCopyFromDb = bookkCopyRepository.getBookCopyByUniqueBookMark("1111111111-1").get();

        Reservation reservation = new Reservation(
                new SecureRandom().nextLong(), bookCopyFromDb.getBookCopyID(), new Date()
        );

        reservationRepository.save(reservation);

        List<Library> libraries = libraryRepository.findAll();

        assertThrows(ReservedByOtherException.class, () -> {
            borrowService.lendOnlyOneReservedBookToUser(
                    "1111111111-1",
                    (long) 0,
                    libraries,

                    userFromDb,
                    "admin"
            );
        });

    }

    @Transactional
    @Test
    public void lendOnlyOneReservedBookToUser_LibraryWrong() {
        BookCopy bookCopy = new BookCopy(
                BookCopy.AVAILABLE,
                "1111111111",
                "1111111111-1",
                (long) 1,
                null,
                null,
                (long) 1
        );
        User user = new User(
                "newUser",
                "password",
                "zhj@email.com",
                User.POSTGRADUATE,
                User.MAX_CREDIT
        );


        userRepository.save(user);
        bookkCopyRepository.save(bookCopy);

        User userFromDb = userRepository.getUserByUsername("newUser");
        BookCopy bookCopyFromDb = bookkCopyRepository.getBookCopyByUniqueBookMark("1111111111-1").get();

        Reservation reservation = new Reservation(
                user.getUser_id(), bookCopyFromDb.getBookCopyID(), new Date()
        );

        reservationRepository.save(reservation);

        List<Library> libraries = libraryRepository.findAll();

        assertThrows(BookCopyNotHereException.class, () -> {
            borrowService.lendOnlyOneReservedBookToUser(
                    "1111111111-1",
                    new SecureRandom().nextLong(),
                    libraries,

                    userFromDb,
                    "admin"
            );
        });

    }

    @Transactional
    @Test
    public void lendOnlyOneReservedBookToUser_Success() {
        BookCopy bookCopy = new BookCopy(
                BookCopy.AVAILABLE,
                "1111111111",
                "1111111111-1",
                (long) 1,
                null,
                null,
                (long) 1
        );
        User user = new User(
                "newUser",
                "password",
                "zhj@email.com",
                User.POSTGRADUATE,
                User.MAX_CREDIT
        );


        userRepository.save(user);
        bookkCopyRepository.save(bookCopy);

        User userFromDb = userRepository.getUserByUsername("newUser");
        BookCopy bookCopyFromDb = bookkCopyRepository.getBookCopyByUniqueBookMark("1111111111-1").get();

        Reservation reservation = new Reservation(
                user.getUser_id(), bookCopyFromDb.getBookCopyID(), new Date()
        );

        reservationRepository.save(reservation);

        List<Library> libraries = libraryRepository.findAll();

        borrowService.lendOnlyOneReservedBookToUser(
                "1111111111-1",
                (long) 1,
                libraries,

                userFromDb,
                "admin"
        );

        Optional<Reservation> reservationFromDB = reservationRepository.getReservationByBookCopyID(bookCopyFromDb.getBookCopyID());
        assertFalse(reservationFromDB.isPresent());

        bookCopyFromDb = bookkCopyRepository.getBookCopyByUniqueBookMark("1111111111-1").get();
        assertEquals(bookCopyFromDb.getStatus(), BookCopy.BORROWED);

        UserConfiguration userConfiguration = userConfigurationRepository.findUserConfigurationByRole(user.getRole()).get();

        Optional<Borrow> borrowOptional = borrowRepository.getBorrowByUniqueBookMark("1111111111-1");
        long timeGap = borrowOptional.get().getDeadline().getTime() - borrowOptional.get().getBorrowDate().getTime();
        assertEquals(timeGap, userConfiguration.getMaxBorrowTime() * 1000);
        assertEquals(borrowOptional.get().getUserID().longValue(), userFromDb.getUser_id());
        assertEquals(borrowOptional.get().getUniqueBookMark(), "1111111111-1");
        assertNotNull(borrowOptional.get().getBorrowDate());

    }


}