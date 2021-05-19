package com.example.lab2.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmailServiceTest {

    @Resource(name = "emailService")
    EmailService emailService;

    @Test
    public void testSendRegisterCaptcha() throws Exception {
        emailService.sendRegisterCaptcha("zhj630985214@gmail.com");
    }


}