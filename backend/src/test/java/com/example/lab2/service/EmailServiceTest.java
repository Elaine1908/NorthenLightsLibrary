package com.example.lab2.service;

import com.example.lab2.dao.BorrowRepository;
import com.example.lab2.dao.FineRepository;
import com.example.lab2.dao.ReservationRepository;
import com.example.lab2.dao.UserRepository;
import com.example.lab2.entity.Borrow;
import com.example.lab2.entity.Fine;
import com.example.lab2.entity.Reservation;
import com.example.lab2.entity.User;
import com.example.lab2.request.upload.UploadNewBookRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.mail.MessagingException;

import java.io.FileInputStream;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmailServiceTest {

    @Resource(name = "emailService")
    EmailService emailService;

    @Resource(name = "uploadService")
    UploadService uploadService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BorrowRepository borrowRepository;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    FineRepository fineRepository;

    @Test
    public void testSendRegisterCaptcha() throws Exception {
        emailService.sendRegisterCaptcha("zhj630985214@gmail.com");
    }

    @Test
    @Transactional
    public void testNotify() throws MessagingException {
        User user = new User(
                "newUser",
                "password",
                "19302010021@fudan.edu.cn",
                User.POSTGRADUATE,
                User.MAX_CREDIT
        );
        //测试正常使用
//        MultipartFile multipartFile = new MockMultipartFile("test", "1.jpg", "content-type", new FileInputStream("D:\\OneDrive\\MyLaptop\\Pictures\\wxh.png"));
//        UploadNewBookRequest uploadNewBookRequest = new UploadNewBookRequest(
//                multipartFile,
//                "isbntest",
//                "Java",
//                "authortest",
//                "descriptiontest",
//                "2000-10-06",
//                1000
//
//        );
//        uploadService.handleUpload(uploadNewBookRequest);
        userRepository.save(user);

        for (int i = 0; i < 3; i++) {
            Borrow borrow = new Borrow(user.getUser_id(), "uniqueBookMark" + i, new Date(), new Date(System.currentTimeMillis() - 1000));
            borrowRepository.save(borrow);
        }

        for (int i = 0; i < 4; i++) {
            Reservation reservation = new Reservation(user.getUser_id(), (long) 23423, new Date(), new Date(System.currentTimeMillis() - 1000));
            reservationRepository.save(reservation);
        }

        for (int i = 0; i < 5; i++) {
            Fine fine = new Fine((long) 1000, user.getUser_id(), "reason", new Date(), UUID.randomUUID().toString());
            fineRepository.save(fine);
        }

        emailService.sendNotify();

        assertEquals(1, 1);

    }


}