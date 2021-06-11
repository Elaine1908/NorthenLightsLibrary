package com.example.lab2.service;

import com.example.lab2.dao.*;
import com.example.lab2.dao.record.BorrowRecordRepository;
import com.example.lab2.dao.record.CreditRecordRepository;
import com.example.lab2.dao.record.ReserveRecordRepository;
import com.example.lab2.dao.record.ReturnRecordRepository;
import com.example.lab2.dto.commentreply.ReplyDTO;
import com.example.lab2.dto.record.*;
import com.example.lab2.entity.*;
import com.example.lab2.exception.borrow.NotBorrowedException;
import com.example.lab2.exception.comment.*;
import com.example.lab2.exception.notfound.*;
import com.example.lab2.request.borrow.ReturnSingleBookRequest;
import com.example.lab2.request.upload.UploadNewBookRequest;
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
import javax.validation.constraints.AssertTrue;

import java.awt.print.Book;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.*;
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

    @Autowired
    BookTypeRepository bookTypeRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    ReplyRepository replyRepository;

    @Resource(name = "normalUserService")
    NormalUserService normalUserService;

    @Resource(name = "uploadService")
    UploadService uploadService;

    @Autowired
    FineRepository fineRepository;

    @Autowired
    BorrowRecordRepository borrowRecordRepository;

    @Autowired
    ReserveRecordRepository reserveRecordRepository;

    @Autowired
    ReturnRecordRepository returnRecordRepository;

    @Autowired
    CreditRecordRepository creditRecordRepository;

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
                    new Date(),
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

        Borrow borrow = new Borrow(userFromDB.getUser_id(), "uniqueBookMark", new Date(), new Date());

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

        Borrow borrow = new Borrow(userFromDB.getUser_id(), "uniqueBookMark", new Date(), new Date());

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

        Borrow borrow = new Borrow(userFromDB.getUser_id(), "uniqueBookMark", new Date(), new Date(System.currentTimeMillis() - 100));

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
        assertEquals(fineList.get(0).getMoney(), 1000);

    }

    @Transactional
    @Test
    public void testReturnOnlyOneBook_Damaged() throws Exception {
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
        assertEquals(fineList.get(0).getMoney(), 500);
    }

    @Transactional
    @Test
    public void testReturnBooks_Success() {
        BookCopy bookCopy1 = new BookCopy(
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

        Borrow borrow1 = new Borrow(user.getUser_id(), "uniqueBookMark1", new Date(), new Date());
        Borrow borrow2 = new Borrow(user.getUser_id(), "uniqueBookMark2", new Date(), new Date());
        Borrow borrow3 = new Borrow(user.getUser_id(), "uniqueBookMark3", new Date(), new Date());
        Borrow borrow4 = new Borrow(user.getUser_id(), "uniqueBookMark4", new Date(), new Date());

        borrowRepository.save(borrow1);
        borrowRepository.save(borrow2);
        borrowRepository.save(borrow3);
        borrowRepository.save(borrow4);

        List<ReturnSingleBookRequest> uniqueBookMarkList = Stream.of(
                new ReturnSingleBookRequest("uniqueBookMark1", "ok"),
                new ReturnSingleBookRequest("uniqueBookMark2", "ok"),
                new ReturnSingleBookRequest("uniqueBookMark3", "damaged"),
                new ReturnSingleBookRequest("uniqueBookMark4", "lost"),
                new ReturnSingleBookRequest("non_existent", "ok")
        ).collect(Collectors.toList());

        List<String> response = normalUserService.returnBooks(uniqueBookMarkList, (long) 4, "admin");

        BookCopy bc = bookCopyRepository.getBookCopyByUniqueBookMark("uniqueBookMark1").get();

        assertTrue(bookCopyRepository.getBookCopyByUniqueBookMark("uniqueBookMark1").isPresent());
        assertEquals(bookCopyRepository.getBookCopyByUniqueBookMark("uniqueBookMark1").get().getStatus(),
                BookCopy.AVAILABLE);
        assertTrue(bookCopyRepository.getBookCopyByUniqueBookMark("uniqueBookMark2").isPresent());
        assertEquals(bookCopyRepository.getBookCopyByUniqueBookMark("uniqueBookMark2").get().getStatus(),
                BookCopy.AVAILABLE);
        assertTrue(bookCopyRepository.getBookCopyByUniqueBookMark("uniqueBookMark3").isPresent());
        assertEquals(bookCopyRepository.getBookCopyByUniqueBookMark("uniqueBookMark3").get().getStatus(),
                BookCopy.DAMAGED);
        assertTrue(bookCopyRepository.getBookCopyByUniqueBookMark("uniqueBookMark4").isPresent());
        assertEquals(bookCopyRepository.getBookCopyByUniqueBookMark("uniqueBookMark4").get().getStatus(),
                BookCopy.LOST);
        assertFalse(borrowRepository.getBorrowByUniqueBookMark("uniqueBookMark1").isPresent());
        assertFalse(borrowRepository.getBorrowByUniqueBookMark("uniqueBookMark2").isPresent());
        assertFalse(borrowRepository.getBorrowByUniqueBookMark("uniqueBookMark3").isPresent());
        assertFalse(borrowRepository.getBorrowByUniqueBookMark("uniqueBookMark4").isPresent());

        response.forEach(System.out::println);

    }

    @Transactional
    @Test
    public void testGetReserveRecord() {

        User user = new User(
                "newUser",
                "password",
                "zhj@email.com",
                User.STUDENT,
                User.MAX_CREDIT
        );
        userRepository.save(user);

        for (int i = 0; i < 40; ++i) {

            ReserveRecord reserveRecord = new ReserveRecord(user.getUser_id(), new Date(), "u" + i, "admin", 1);

            reserveRecordRepository.save(reserveRecord);
        }

        assertEquals(normalUserService.getReserveRecord("newUser").size(), 40);

    }

    @Transactional
    @Test
    public void testGetBorrowRecord() {

        User user = new User(
                "newUser",
                "password",
                "zhj@email.com",
                User.STUDENT,
                User.MAX_CREDIT
        );
        userRepository.save(user);

        for (int i = 0; i < 40; ++i) {
            BorrowRecord borrowRecord = new BorrowRecord(user.getUser_id(), new Date(), "u" + i, "admin", 1);
            borrowRecordRepository.save(borrowRecord);
        }

        assertEquals(normalUserService.getBorrowRecord("newUser").size(), 40);

    }

    @Transactional
    @Test
    public void testGetReturnRecord() {

        User user = new User(
                "newUser",
                "password",
                "zhj@email.com",
                User.STUDENT,
                User.MAX_CREDIT
        );
        userRepository.save(user);

        for (int i = 0; i < 40; ++i) {
            ReturnRecord returnRecord = new ReturnRecord(user.getUser_id(), new Date(), "u" + i, "admin", 1,ReturnRecord.OK);
            returnRecordRepository.save(returnRecord);
        }

        assertEquals(normalUserService.getReturnRecord("newUser").size(), 40);

    }

    @Transactional
    @Test
    public void testGetBookCopyRecord() {
        BookCopy bookCopy = new BookCopy(
                BookCopy.AVAILABLE,
                "isbn",
                "uniqueBookMark",
                (long) 1,
                new Date(),
                new Date(),
                (long) 2
        );

        bookCopyRepository.save(bookCopy);

        SecureRandom secureRandom = new SecureRandom();

        int reserveRecordAmount = secureRandom.nextInt(10);
        int borrowRecordAmount = secureRandom.nextInt(10);
        int returnRecordAmount = secureRandom.nextInt(10);

        for (int i = 0; i < reserveRecordAmount; i++) {
            ReserveRecord reserveRecord = new ReserveRecord(
                    secureRandom.nextLong(),
                    new Date(),
                    "uniqueBookMark",
                    "admin",
                    1
            );
            reserveRecordRepository.save(reserveRecord);
        }

        for (int i = 0; i < borrowRecordAmount; i++) {
            BorrowRecord borrowRecord = new BorrowRecord(
                    secureRandom.nextLong(),
                    new Date(),
                    "uniqueBookMark",
                    "admin",
                    1
            );
            borrowRecordRepository.save(borrowRecord);
        }

        for (int i = 0; i < returnRecordAmount; i++) {
            ReturnRecord returnRecord = new ReturnRecord(secureRandom.nextLong(),
                    new Date(),
                    "uniqueBookMark",
                    "admin",
                    1,ReturnRecord.OK);
            returnRecordRepository.save(returnRecord);
        }

        List<RecordAboutBookCopyDTO> lis = normalUserService.getBookCopyRecord("uniqueBookMark");
        HashMap<Class<?>, Integer> hashMap = new HashMap<>();
        lis.forEach((RecordAboutBookCopyDTO r) -> {
            if (!hashMap.containsKey(r.getClass())) {
                hashMap.put(r.getClass(), 0);
            }
            hashMap.put(r.getClass(), hashMap.get(r.getClass()) + 1);
        });
        assertEquals(hashMap.get(BorrowRecordDTO.class).intValue(), borrowRecordAmount);
        assertEquals(hashMap.get(ReserveRecordDTO.class).intValue(), reserveRecordAmount);
        assertEquals(hashMap.get(ReturnRecordDTO.class).intValue(), returnRecordAmount);


    }

    @Test
    @Transactional
    public void testGetCreditRecordListByUsername() {

        User user = new User(
                "newUser",
                "password",
                "zhj@email.com",
                User.UNDERGRADUATE,
                User.MAX_CREDIT
        );
        userRepository.save(user);

        for (int i = 0; i < 10; i++) {
            CreditRecord cr = new CreditRecord(user.getUser_id(), 10, "test", new Date());
            creditRecordRepository.save(cr);
        }

        List<CreditRecordDTO> creditRecordDTOList = normalUserService.getCreditRecordListByUsername("newUser");

        assertEquals(creditRecordDTOList.size(), 10);

        creditRecordDTOList.forEach(creditRecordDTO -> {
            assertEquals(creditRecordDTO.getDescription(), "test");
            assertEquals(creditRecordDTO.getAmount(), 10);

        });

    }

    @Test
    @Transactional
    public void testComment_UserNotFound(){
        assertThrows(UserNotFoundException.class, () -> {
            normalUserService.postComment("non_existent_user","111","like it",10);
        });
    }

    @Test
    @Transactional
    public void testComment_BookTypeNotFound(){
        User user = new User(
                "newUser",
                "password",
                "zyw@email.com",
                User.STUDENT,
                User.MAX_CREDIT
        );
        userRepository.save(user);

        assertThrows(BookTypeNotFoundException.class, () -> {
            normalUserService.postComment("newUser","non_existent_isbn","like it",10);
        });

    }

    @Test
    @Transactional
    public void testComment_RateOutOfRange(){
        User user = new User(
                "newUser",
                "password",
                "zyw@email.com",
                User.STUDENT,
                User.MAX_CREDIT
        );
        userRepository.save(user);

        BookType b = new BookType();
        b.setIsbn("isbn");
        b.setName("name");
        b.setAuthor("author");
        b.setDescription("description");
        b.setPrice(100);
        bookTypeRepository.save(b);

        assertThrows(RateOutOfRangeException.class, () -> {
            normalUserService.postComment("newUser","isbn","like it",100);
        });

    }

    @Test
    @Transactional
    public void testComment_AlreadyCommented(){
        User user = new User(
                "newUser",
                "password",
                "zyw@email.com",
                User.STUDENT,
                User.MAX_CREDIT
        );
        userRepository.save(user);

        BookType b = new BookType();
        b.setIsbn("isbn");
        b.setName("name");
        b.setAuthor("author");
        b.setDescription("description");
        b.setPrice(100);
        bookTypeRepository.save(b);

        User userFromDB = userRepository.getUserByUsername("newUser");

        Comment comment = new Comment(userFromDB.getUser_id(),"isbn","like",new Date(),false,false,8);
        commentRepository.save(comment);

        assertThrows(CommentAlreadyExistException.class, () -> {
            normalUserService.postComment("newUser","isbn","like it",10);
        });

    }

    @Test
    @Transactional
    public void testComment_NoReturnRecord(){
        User user = new User(
                "newUser",
                "password",
                "zyw@email.com",
                User.STUDENT,
                User.MAX_CREDIT
        );
        userRepository.save(user);

        BookType b = new BookType();
        b.setIsbn("isbn");
        b.setName("name");
        b.setAuthor("author");
        b.setDescription("description");
        b.setPrice(100);
        bookTypeRepository.save(b);

        User userFromDB = userRepository.getUserByUsername("newUser");

        assertThrows(NoReturnRecordException.class, () -> {
            normalUserService.postComment("newUser","isbn","like it",10);
        });

    }

    @Test
    @Transactional
    public void testComment_BadReturnRecord(){
        User user = new User(
                "newUser",
                "password",
                "zyw@email.com",
                User.STUDENT,
                User.MAX_CREDIT
        );
        userRepository.save(user);

        BookType b = new BookType();
        b.setIsbn("isbn");
        b.setName("name");
        b.setAuthor("author");
        b.setDescription("description");
        b.setPrice(100);
        bookTypeRepository.save(b);

        User userFromDB = userRepository.getUserByUsername("newUser");
        ReturnRecord returnRecord = new ReturnRecord(userFromDB.getUser_id(), new Date(), "isbn-001", "admin", 1,ReturnRecord.DAMAGED);
        returnRecordRepository.save(returnRecord);

        assertThrows(ReturnRecordNotOkException.class, () -> {
            normalUserService.postComment("newUser","isbn","like it",10);
        });

    }

    @Test
    @Transactional
    public void testComment_Success(){
        User user = new User(
                "newUser",
                "password",
                "zyw@email.com",
                User.STUDENT,
                User.MAX_CREDIT
        );
        userRepository.save(user);
        User userFromDB = userRepository.getUserByUsername("newUser");

        BookType b = new BookType();
        b.setIsbn("isbn");
        b.setName("name");
        b.setAuthor("author");
        b.setDescription("description");
        b.setPrice(100);
        bookTypeRepository.save(b);

        ReturnRecord returnRecord = new ReturnRecord(userFromDB.getUser_id(),new Date(),"isbn-001","admin",1,ReturnRecord.OK);
        returnRecordRepository.save(returnRecord);

        Comment comment = new Comment(userFromDB.getUser_id(),"isbn","good",new Date(),false,false,8);
        commentRepository.save(comment);
        Optional<Comment> commentFromDB = commentRepository.getCommentByUserID(userFromDB.getUser_id());
        assertTrue(commentFromDB.isPresent());
        assertEquals(commentFromDB.get().getUserID(),userFromDB.getUser_id());
        assertEquals(commentFromDB.get().getIsbn(),"isbn");
        assertEquals(commentFromDB.get().getContent(),"good");
    }

    @Test
    @Transactional
    public void testReply_UserNotFound(){
        assertThrows(UserNotFoundException.class, () -> {
            normalUserService.postReply("non_existent_user",(long)1,(long)1,"like it");
        });
    }

    @Test
    @Transactional
    public void testReply_TwoIdAtTheSameTime(){
        User user = new User(
                "newUser",
                "password",
                "zyw@email.com",
                User.STUDENT,
                User.MAX_CREDIT
        );
        userRepository.save(user);

        assertThrows(TwoIDAtTheSameTimeException.class, () -> {
            normalUserService.postReply("newUser",(long)1,(long)1,"like it");
        });

    }

    @Test
    @Transactional
    public void testReply_NonExistentComment(){
        User user = new User(
                "newUser",
                "password",
                "zyw@email.com",
                User.STUDENT,
                User.MAX_CREDIT
        );
        userRepository.save(user);

        assertThrows(CommentNotFoundException.class, () -> {
            normalUserService.postReply("newUser",(long)8888,null,"like it");
        });

    }

    @Test
    @Transactional
    public void testReply_NonExistentReply(){
        User user = new User(
                "newUser",
                "password",
                "zyw@email.com",
                User.STUDENT,
                User.MAX_CREDIT
        );
        userRepository.save(user);

        assertThrows(CommentNotFoundException.class, () -> {
            normalUserService.postReply("newUser",null,(long)8888,"like it");
        });

    }

    @Test
    @Transactional
    public void testReply_Success_Comment(){
        User user = new User(
                "newUser",
                "password",
                "zyw@email.com",
                User.STUDENT,
                User.MAX_CREDIT
        );
        userRepository.save(user);
        User userFromDB = userRepository.getUserByUsername("newUser");

        User user2 = new User(
                "newUser2",
                "password2",
                "zyw2@email.com",
                User.STUDENT,
                User.MAX_CREDIT
        );
        userRepository.save(user2);
        User userFromDB2 = userRepository.getUserByUsername("newUser2");

        BookType b = new BookType();
        b.setIsbn("isbn");
        b.setName("name");
        b.setAuthor("author");
        b.setDescription("description");
        b.setPrice(100);
        bookTypeRepository.save(b);

        ReturnRecord returnRecord = new ReturnRecord(userFromDB.getUser_id(),new Date(),"isbn-001","admin",1,ReturnRecord.OK);
        returnRecordRepository.save(returnRecord);

        Comment comment = new Comment(userFromDB.getUser_id(),"isbn","good",new Date(),false,false,8);
        commentRepository.save(comment);
        Optional<Comment> commentFromDB = commentRepository.getCommentByUserID(userFromDB.getUser_id());
        assertTrue(commentFromDB.isPresent());

        Reply reply = new Reply(userFromDB2.getUser_id(),commentFromDB.get().getCommendID(),"content",new Date(),false,false,commentFromDB.get().getUserID());
        replyRepository.save(reply);

        List<ReplyDTO> replies = replyRepository.getRepliesByCommentID(commentFromDB.get().getCommendID());
        assertFalse(replies.isEmpty());
        assertEquals(replies.get(0).getRepliedUsername(),"newUser");
        assertEquals(replies.get(0).getUsername(),"newUser2");
    }

    @Test
    @Transactional
    public void testDeleteComment_UserNotFound(){
        assertThrows(UserNotFoundException.class, () -> {
            normalUserService.deleteComment((long)1,"non_existent_user");
        });
    }

    @Test
    @Transactional
    public void testDeleteComment_CommentNotFound(){
        User user = new User(
                "newUser",
                "password",
                "zyw@email.com",
                User.STUDENT,
                User.MAX_CREDIT
        );
        userRepository.save(user);
        assertThrows(CommentNotFoundException.class, () -> {
            normalUserService.deleteComment((long)8888,"newUser");
        });
    }

    @Test
    @Transactional
    public void testDeleteComment_CommentMismatch() {
        User user = new User(
                "newUser",
                "password",
                "zyw@email.com",
                User.STUDENT,
                User.MAX_CREDIT
        );
        userRepository.save(user);
        User userFromDB = userRepository.getUserByUsername("newUser");

        Comment comment = new Comment(userFromDB.getUser_id()+1,"isbn","good",new Date(),false,false,8);
        commentRepository.save(comment);
        Optional<Comment> commentFromDB = commentRepository.getCommentByUserID(userFromDB.getUser_id()+1);
        assertTrue(commentFromDB.isPresent());

        assertThrows(CommentMismatchException.class, () -> {
            normalUserService.deleteComment(commentFromDB.get().getCommendID(),"newUser");
        });

    }

    @Test
    @Transactional
    public void testDeleteComment_Success() {
        User user = new User(
                "newUser",
                "password",
                "zyw@email.com",
                User.STUDENT,
                User.MAX_CREDIT
        );
        userRepository.save(user);
        User userFromDB = userRepository.getUserByUsername("newUser");

        Comment comment = new Comment(userFromDB.getUser_id(),"isbn","good",new Date(),false,false,8);
        commentRepository.save(comment);
        Optional<Comment> commentFromDB = commentRepository.getCommentByUserID(userFromDB.getUser_id());
        assertTrue(commentFromDB.isPresent());

        normalUserService.deleteComment(commentFromDB.get().getCommendID(),"newUser");

        Optional<Comment> commentFromDB2 = commentRepository.getCommentByUserID(userFromDB.getUser_id());
        assertFalse(commentFromDB2.isPresent());


    }

    @Test
    @Transactional
    public void testDeleteReply_ReplyNotFound(){
        User user = new User(
                "newUser",
                "password",
                "zyw@email.com",
                User.STUDENT,
                User.MAX_CREDIT
        );
        userRepository.save(user);

        assertThrows(CommentNotFoundException.class, () -> {
            normalUserService.deleteReply((long)8888,"newUser");
        });
    }

    @Test
    @Transactional
    public void testDeleteReply_Success(){
        User user = new User(
                "newUser",
                "password",
                "zyw@email.com",
                User.STUDENT,
                User.MAX_CREDIT
        );
        userRepository.save(user);
        User userFromDB = userRepository.getUserByUsername("newUser");

        Comment comment = new Comment(userFromDB.getUser_id(),"isbn","good",new Date(),false,false,8);
        commentRepository.save(comment);
        Optional<Comment> commentFromDB = commentRepository.getCommentByUserID(userFromDB.getUser_id());
        assertTrue(commentFromDB.isPresent());
        Reply reply = new Reply(userFromDB.getUser_id(),commentFromDB.get().getCommendID(),"ok",new Date(),false,false,userFromDB.getUser_id());
        replyRepository.save(reply);
        List<Reply> replyFromDB = replyRepository.findAllByCommentID(commentFromDB.get().getCommendID());
        assertEquals(replyFromDB.size(),1);
        normalUserService.deleteReply(replyFromDB.get(0).getReplyID(),"newUser");

        List<Reply> replyFromDB2 = replyRepository.findAllByCommentID(commentFromDB.get().getCommendID());
        assertEquals(replyFromDB2.size(),0);

    }

}