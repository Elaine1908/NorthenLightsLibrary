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
import com.example.lab2.response.GeneralResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.swing.text.html.Option;

import java.security.SecureRandom;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    public void testLendUniquebookMarkListToUser() {
        User user = new User(
                "newUser",
                "password",
                "zhj@email.com",
                User.POSTGRADUATE,
                User.MAX_CREDIT
        );

        userRepository.save(user);
        BookCopy bookCopy1 = new BookCopy(
                BookCopy.AVAILABLE,
                "isbn1",
                "uniqueBookMark1",
                (long) 1,
                null,
                null,
                (long) 1
        );
        BookCopy bookCopy2 = new BookCopy(
                BookCopy.AVAILABLE,
                "isbn1",
                "uniqueBookMark2",
                (long) 2,
                null,
                null,
                (long) 1
        );
        BookCopy bookCopy3 = new BookCopy(
                BookCopy.DAMAGED,
                "isbn1",
                "uniqueBookMark3",
                (long) 2,
                null,
                null,
                (long) 1
        );

        bookkCopyRepository.save(bookCopy1);
        bookkCopyRepository.save(bookCopy2);
        bookkCopyRepository.save(bookCopy3);

        GeneralResponse response = borrowService.lendBookToUser(
                Stream.of("uniqueBookMark1", "uniqueBookMark2", "uniqueBookMark3", "non_existend").collect(Collectors.toList()),
                "newUser",
                (long) 1,
                "admin"
        );

        System.out.println(response.getMessage());


    }

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
        List<Library> libraries = libraryRepository.findAll();

        Optional<UserConfiguration> userConfigurationOptional = userConfigurationRepository.findUserConfigurationByRole(User.POSTGRADUATE);

        assertThrows(BookCopyNotFoundException.class, () -> {
            borrowService.lendOnlyOneBookToUser(
                    "non_existent_book", (long) 1, libraries, user, "admin", userConfigurationOptional
            );
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

        List<Library> libraries = libraryRepository.findAll();

        Optional<UserConfiguration> userConfigurationOptional = userConfigurationRepository.findUserConfigurationByRole(User.POSTGRADUATE);
        userRepository.save(user);
        bookkCopyRepository.save(bookCopy);
        assertThrows(BookCopyNotHereException.class, () -> {
            borrowService.lendOnlyOneBookToUser(
                    "1111111111-1", (long) 4, libraries, user, "admin", userConfigurationOptional
            );
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
                (long) 323545, bookCopyFromDB.getBookCopyID(), new Date()
        );

        reservationRepository.save(reservation);


        List<Library> libraries = libraryRepository.findAll();

        Optional<UserConfiguration> userConfigurationOptional = userConfigurationRepository.findUserConfigurationByRole(User.POSTGRADUATE);


        assertThrows(BookCopyReservedException.class, () -> {
            borrowService.lendOnlyOneBookToUser(
                    "1111111111-1", (long) 1, libraries, user, "admin", userConfigurationOptional
            );
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
                (long) 345345, "1111111111-1", new Date(), new Date()
        );
        borrowRepository.save(borrow);

        List<Library> libraries = libraryRepository.findAll();

        Optional<UserConfiguration> userConfigurationOptional = userConfigurationRepository.findUserConfigurationByRole(User.POSTGRADUATE);

        assertThrows(BookCopyIsBorrowedException.class, () -> {
            borrowService.lendOnlyOneBookToUser(
                    "1111111111-1", (long) 1, libraries, user, "admin", userConfigurationOptional
            );
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

        List<Library> libraries = libraryRepository.findAll();

        Optional<UserConfiguration> userConfigurationOptional = userConfigurationRepository.findUserConfigurationByRole(User.POSTGRADUATE);

        borrowService.lendOnlyOneBookToUser(
                "1111111111-1", (long) 1, libraries, user, "admin", userConfigurationOptional
        );
        Borrow borrowFromDb = borrowRepository.getBorrowByUniqueBookMark("1111111111-1").get();
        User userFromDb = userRepository.getUserByUsername("newUser");
        BookCopy bookCopyFromDb = bookkCopyRepository.getBookCopyByUniqueBookMark("1111111111-1").get();
        long timegap = borrowFromDb.getDeadline().getTime() - borrowFromDb.getBorrowDate().getTime();

        assertEquals(timegap, 1000 * userConfigurationOptional.get().getMaxBorrowTime());
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