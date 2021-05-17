package com.example.lab2.service;

import com.example.lab2.dao.*;
import com.example.lab2.entity.*;
import com.example.lab2.exception.borrow.NotBorrowedException;
import com.example.lab2.exception.notfound.BookCopyNotFoundException;
import com.example.lab2.exception.notfound.LibraryNotFoundException;
import com.example.lab2.request.borrow.ReturnSingleBookRequest;
import com.example.lab2.request.upload.UploadNewBookRequest;
import com.example.lab2.response.GeneralResponse;
import com.example.lab2.response.UserInfoResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @Autowired
    BookCopyRepository bookCopyRepository;

    @Resource(name = "normalUserService")
    NormalUserService normalUserService;

    @Resource(name = "uploadService")
    UploadService uploadService;

    @Autowired
    FineRepository fineRepository;

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

    @Transactional
    @Test
    public void testReturnOnlyOneBook_BookCopyNotFound() {
        assertThrows(BookCopyNotFoundException.class, () -> {
            normalUserService.returnOnlyOneBook(
                    new ReturnSingleBookRequest(
                            "non_existent", "ok"
                    ), (long) 4, "admin"
            );

        });


    }

    @Transactional
    @Test
    public void testReturnOnlyOneBook_BookCopyNotBorrowed() {
        BookCopy bookCopy = new BookCopy(
                BookCopy.AVAILABLE,
                "isbn",
                "uniqueBookMark",
                (long) 1,
                null,
                null,
                (long) 1
        );

        bookCopyRepository.save(bookCopy);
        assertThrows(NotBorrowedException.class, () -> {
            normalUserService.returnOnlyOneBook(
                    new ReturnSingleBookRequest("uniqueBookMark", "ok"),
                    (long) 0,
                    "admin"
            );
        });


    }

    @Transactional
    @Test

    public void testReturnOnlyOneBook_LibraryNotFound() {
        BookCopy bookCopy = new BookCopy(
                BookCopy.BORROWED,
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
        bookCopyRepository.save(bookCopy);

        User userFromDB = userRepository.getUserByUsername("newUser");

        Borrow borrow = new Borrow(userFromDB.getUser_id(), "uniqueBookMark", new Date());

        borrowRepository.save(borrow);

        assertThrows(LibraryNotFoundException.class, () -> {
            normalUserService.returnOnlyOneBook(
                    new ReturnSingleBookRequest("uniqueBookMark", "ok"), (long) 9999, "admin"
            );

        });


    }

    @Transactional
    @Test

    public void testReturnOnlyOneBook_AdminNotFound() {
        BookCopy bookCopy = new BookCopy(
                BookCopy.BORROWED,
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
        bookCopyRepository.save(bookCopy);

        User userFromDB = userRepository.getUserByUsername("newUser");

        Borrow borrow = new Borrow(userFromDB.getUser_id(), "uniqueBookMark", new Date());

        borrowRepository.save(borrow);

        assertThrows(LibraryNotFoundException.class, () -> {
            normalUserService.returnOnlyOneBook(
                    new ReturnSingleBookRequest("uniqueBookMark", "ok"), (long) 9999, "non_existient_admin"
            );

        });


    }


    @Transactional
    @Test
    public void testReturnOnlyOneBook_OK() throws IOException {
        BookCopy bookCopy = new BookCopy(
                BookCopy.BORROWED,
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
        MultipartFile multipartFile = new MockMultipartFile("test", "1.jpg", "content-type", new FileInputStream("D:\\OneDrive\\MyLaptop\\Pictures\\wxh.png"));
        UploadNewBookRequest uploadNewBookRequest = new UploadNewBookRequest(
                multipartFile,
                "isbn",
                "nametest",
                "authortest",
                "descriptiontest",
                "2000-10-06",
                1000

        );

        uploadService.handleUpload(uploadNewBookRequest);
        userRepository.save(user);
        bookCopyRepository.save(bookCopy);

        User userFromDB = userRepository.getUserByUsername("newUser");

        Borrow borrow = new Borrow(userFromDB.getUser_id(), "uniqueBookMark", new Date(), new Date(System.currentTimeMillis() + (long) 30 * 24 * 3600 * 1000));

        borrowRepository.save(borrow);

        normalUserService.returnOnlyOneBook(
                new ReturnSingleBookRequest("uniqueBookMark", "ok"), (long) 4, "admin"
        );

        Optional<Borrow> borrowOptional = borrowRepository.getBorrowByUniqueBookMark("uniqueBookMark");
        assertFalse(borrowOptional.isPresent());

        BookCopy bookCopyFromDB = bookCopyRepository.getBookCopyByUniqueBookMark("uniqueBookMark").get();

        assertEquals(bookCopyFromDB.getStatus(), BookCopy.AVAILABLE);
        assertEquals(bookCopyFromDB.getUniqueBookMark(), "uniqueBookMark");

        assertEquals(bookCopyFromDB.getLibraryID().longValue(), 4);

        assertNotNull(bookCopyFromDB.getLastReturnDate());

        User admin = userRepository.getUserByUsername("admin");
        assertEquals(admin.getUser_id(), bookCopy.getAdminID().longValue());

    }

    @Transactional
    @Test
    public void testReturnOnlyOneBook_OK_OverTime() throws IOException {
        BookCopy bookCopy = new BookCopy(
                BookCopy.BORROWED,
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
        MultipartFile multipartFile = new MockMultipartFile("test", "1.jpg", "content-type", new FileInputStream("D:\\OneDrive\\MyLaptop\\Pictures\\wxh.png"));
        UploadNewBookRequest uploadNewBookRequest = new UploadNewBookRequest(
                multipartFile,
                "isbn",
                "nametest",
                "authortest",
                "descriptiontest",
                "2000-10-06",
                1000

        );

        uploadService.handleUpload(uploadNewBookRequest);
        userRepository.save(user);
        bookCopyRepository.save(bookCopy);

        User userFromDB = userRepository.getUserByUsername("newUser");

        Borrow borrow = new Borrow(userFromDB.getUser_id(), "uniqueBookMark", new Date(), new Date(System.currentTimeMillis()-100));

        borrowRepository.save(borrow);

        System.out.println(normalUserService.returnOnlyOneBook(
                new ReturnSingleBookRequest("uniqueBookMark", "ok"), (long) 4, "admin"
        ));

        Optional<Borrow> borrowOptional = borrowRepository.getBorrowByUniqueBookMark("uniqueBookMark");
        assertFalse(borrowOptional.isPresent());

        BookCopy bookCopyFromDB = bookCopyRepository.getBookCopyByUniqueBookMark("uniqueBookMark").get();

        assertEquals(bookCopyFromDB.getStatus(), BookCopy.AVAILABLE);
        assertEquals(bookCopyFromDB.getUniqueBookMark(), "uniqueBookMark");

        assertEquals(bookCopyFromDB.getLibraryID().longValue(), 4);

        assertNotNull(bookCopyFromDB.getLastReturnDate());

        User admin = userRepository.getUserByUsername("admin");
        assertEquals(admin.getUser_id(), bookCopy.getAdminID().longValue());

        List<Fine> fineList = fineRepository.getFineByUserID(userFromDB.getUser_id());
        assertEquals(fineList.get(0).getMoney(), 1000 / 4);

    }


    @Transactional
    @Test
    public void testReturnOnlyOneBook_Damaged_OverTime() throws IOException {
        BookCopy bookCopy = new BookCopy(
                BookCopy.BORROWED,
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
        MultipartFile multipartFile = new MockMultipartFile("test", "1.jpg", "content-type", new FileInputStream("D:\\OneDrive\\MyLaptop\\Pictures\\wxh.png"));
        UploadNewBookRequest uploadNewBookRequest = new UploadNewBookRequest(
                multipartFile,
                "isbn",
                "nametest",
                "authortest",
                "descriptiontest",
                "2000-10-06",
                1000

        );

        uploadService.handleUpload(uploadNewBookRequest);
        userRepository.save(user);
        bookCopyRepository.save(bookCopy);

        User userFromDB = userRepository.getUserByUsername("newUser");

        Borrow borrow = new Borrow(userFromDB.getUser_id(), "uniqueBookMark", new Date(), new Date(System.currentTimeMillis() + (long) 1));

        borrowRepository.save(borrow);

        System.out.println(normalUserService.returnOnlyOneBook(
                new ReturnSingleBookRequest("uniqueBookMark", "damaged"), (long) 4, "admin"
        ));

        Optional<Borrow> borrowOptional = borrowRepository.getBorrowByUniqueBookMark("uniqueBookMark");
        assertFalse(borrowOptional.isPresent());

        BookCopy bookCopyFromDB = bookCopyRepository.getBookCopyByUniqueBookMark("uniqueBookMark").get();

        assertEquals(bookCopyFromDB.getStatus(), BookCopy.DAMAGED);
        assertEquals(bookCopyFromDB.getUniqueBookMark(), "uniqueBookMark");

        assertEquals(bookCopyFromDB.getLibraryID().longValue(), 4);

        assertNotNull(bookCopyFromDB.getLastReturnDate());

        User admin = userRepository.getUserByUsername("admin");
        assertEquals(admin.getUser_id(), bookCopy.getAdminID().longValue());

        List<Fine> fineList = fineRepository.getFineByUserID(userFromDB.getUser_id());
        assertEquals(fineList.get(0).getMoney(), 1000 / 2);

    }


    @Transactional
    @Test
    public void testReturnOnlyOneBook_Lost_OverTime() throws IOException {
        BookCopy bookCopy = new BookCopy(
                BookCopy.BORROWED,
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
        MultipartFile multipartFile = new MockMultipartFile("test", "1.jpg", "content-type", new FileInputStream("D:\\OneDrive\\MyLaptop\\Pictures\\wxh.png"));
        UploadNewBookRequest uploadNewBookRequest = new UploadNewBookRequest(
                multipartFile,
                "isbn",
                "nametest",
                "authortest",
                "descriptiontest",
                "2000-10-06",
                1000

        );

        uploadService.handleUpload(uploadNewBookRequest);
        userRepository.save(user);
        bookCopyRepository.save(bookCopy);

        User userFromDB = userRepository.getUserByUsername("newUser");

        Borrow borrow = new Borrow(userFromDB.getUser_id(), "uniqueBookMark", new Date(), new Date(System.currentTimeMillis() + (long) 1));

        borrowRepository.save(borrow);

        System.out.println(normalUserService.returnOnlyOneBook(
                new ReturnSingleBookRequest("uniqueBookMark", "lost"), (long) 4, "admin"
        ));

        Optional<Borrow> borrowOptional = borrowRepository.getBorrowByUniqueBookMark("uniqueBookMark");
        assertFalse(borrowOptional.isPresent());

        BookCopy bookCopyFromDB = bookCopyRepository.getBookCopyByUniqueBookMark("uniqueBookMark").get();

        assertEquals(bookCopyFromDB.getStatus(), BookCopy.LOST);
        assertEquals(bookCopyFromDB.getUniqueBookMark(), "uniqueBookMark");

        assertEquals(bookCopyFromDB.getLibraryID().longValue(), 4);

        assertNotNull(bookCopyFromDB.getLastReturnDate());

        User admin = userRepository.getUserByUsername("admin");
        assertEquals(admin.getUser_id(), bookCopy.getAdminID().longValue());

        List<Fine> fineList = fineRepository.getFineByUserID(userFromDB.getUser_id());
        assertEquals(fineList.get(0).getMoney(), 1000 );

    }

    @Transactional
    @Test
    public void testReturnBooks_Success() {
      /*  BookCopy bookCopy1 = new BookCopy(
                BookCopy.BORROWED,
                "isbn",
                "uniqueBookMark1",
                (long) 1,
                null,
                null,
                (long) 1
        );
        BookCopy bookCopy2 = new BookCopy(
                BookCopy.BORROWED,
                "isbn",
                "uniqueBookMark2",
                (long) 1,
                null,
                null,
                (long) 1
        );
        BookCopy bookCopy3 = new BookCopy(
                BookCopy.BORROWED,
                "isbn",
                "uniqueBookMark3",
                (long) 1,
                null,
                null,
                (long) 1
        );
        BookCopy bookCopy4 = new BookCopy(
                BookCopy.BORROWED,
                "isbn",
                "uniqueBookMark4",
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
        bookCopyRepository.save(bookCopy1);
        bookCopyRepository.save(bookCopy2);
        bookCopyRepository.save(bookCopy3);
        bookCopyRepository.save(bookCopy4);

        Borrow borrow1 = new Borrow(user.getUser_id(), "uniqueBookMark1", new Date());
        Borrow borrow2 = new Borrow(user.getUser_id(), "uniqueBookMark2", new Date());
        Borrow borrow3 = new Borrow(user.getUser_id(), "uniqueBookMark3", new Date());
        Borrow borrow4 = new Borrow(user.getUser_id(), "uniqueBookMark4", new Date());

        borrowRepository.save(borrow1);
        borrowRepository.save(borrow2);
        borrowRepository.save(borrow3);
        borrowRepository.save(borrow4);

        List<String> uniqueBookMarkList = Stream.of(
                "uniqueBookMark1",
                "uniqueBookMark2",
                "uniqueBookMark3",
                "uniqueBookMark4",
                "non_existent_mark"
        ).collect(Collectors.toList());

        GeneralResponse response = normalUserService.returnBooks(uniqueBookMarkList, (long) 4, "admin");

        System.out.println(response.getMessage());

        assertTrue(bookCopyRepository.getBookCopyByUniqueBookMark("uniqueBookMark1").isPresent());
        assertTrue(bookCopyRepository.getBookCopyByUniqueBookMark("uniqueBookMark2").isPresent());
        assertTrue(bookCopyRepository.getBookCopyByUniqueBookMark("uniqueBookMark3").isPresent());
        assertTrue(bookCopyRepository.getBookCopyByUniqueBookMark("uniqueBookMark4").isPresent());
        assertFalse(borrowRepository.getBorrowByUniqueBookMark("uniqueBookMark1").isPresent());
        assertFalse(borrowRepository.getBorrowByUniqueBookMark("uniqueBookMark2").isPresent());
        assertFalse(borrowRepository.getBorrowByUniqueBookMark("uniqueBookMark3").isPresent());
        assertFalse(borrowRepository.getBorrowByUniqueBookMark("uniqueBookMark4").isPresent());*/

    }


}