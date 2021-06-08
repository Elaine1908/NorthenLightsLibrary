package com.example.lab2.service;

import com.example.lab2.dao.UserRepository;
import com.example.lab2.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SuperAdminServiceTest {

    @Autowired
    UserRepository userRepository;

    @Resource(name = "superAdminService")
    SuperAdminService superAdminService;

    @Test
    @Transactional
    public void testResetCredit() {

        User user = new User(
                "newUser",
                "password",
                "zhj630985214@gmail.com",
                User.POSTGRADUATE,
                User.MAX_CREDIT
        );
        userRepository.save(user);

        superAdminService.resetCredit("newUser", 70);

        User userFromDB = userRepository.getUserByUsername("newUser");

        assertEquals(userFromDB.getCredit(), 70);
    }

}