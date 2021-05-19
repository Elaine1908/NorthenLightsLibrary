package com.example.lab2.service;

import com.example.lab2.dao.FineRepository;
import com.example.lab2.dao.UserRepository;
import com.example.lab2.entity.Fine;
import com.example.lab2.entity.User;
import com.example.lab2.response.GeneralResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FineServiceTest {


    @Resource(name = "fineService")
    FineService fineService;

    @Autowired
    FineRepository fineRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    @Transactional
    public void testGetAllFine() {
        User user = new User(
                "testuser",
                "testpassword",
                "test@email.com",
                "student",
                100
        );

        userRepository.save(user);

        User userFromDB = userRepository.findByName("testuser").get();

        long userID = userFromDB.getUser_id();

        for (int i = 0; i < 10; i++) {
            Fine fine = new Fine(i, userID, "testReason" + i, new Date(), UUID.randomUUID().toString());
            fineRepository.save(fine);
        }

        List<Fine> fineList = fineService.getAllFine("testuser");
        assertEquals(fineList.size(), 10);
        fineList.sort((Fine f1, Fine f2) -> (int) (f1.getFineID() - f2.getFineID()));
        for (int i = 0; i < fineList.size(); i++) {
            assertEquals(fineList.get(i).getUserID(), userID);
            assertEquals(fineList.get(i).getMoney(), i);
            assertEquals(fineList.get(i).getReason(), "testReason" + i);
        }


    }

    @Test
    @Transactional
    public void testPayFine() {
        User user = new User(
                "testuser",
                "testpassword",
                "test@email.com",
                "student",
                100
        );

        userRepository.save(user);

        User userFromDB = userRepository.findByName("testuser").get();

        long userID = userFromDB.getUser_id();

        for (int i = 0; i < 100; i++) {
            Fine fine = new Fine(i, userID, "testReason" + i, new Date(), UUID.randomUUID().toString());
            fineRepository.save(fine);
        }

        List<Fine> fineList = fineRepository.getFineByUserID(userID);
        for (Fine fine : fineList) {
            GeneralResponse response = fineService.payfine(user.getUsername(), fine.getFineID());
            if (response.getMessage().equals("success")) {
                Optional<Fine> fineFromRepository = fineRepository.findById(fine.getFineID());
                assertTrue(fineFromRepository.isEmpty());
            } else {
                Optional<Fine> fineFromRepository = fineRepository.findById(fine.getFineID());
                assertFalse(fineFromRepository.isEmpty());
            }

        }


    }

}