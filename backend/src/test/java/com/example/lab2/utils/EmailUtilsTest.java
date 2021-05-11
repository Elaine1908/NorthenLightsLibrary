package com.example.lab2.utils;

import com.example.lab2.config.EmailConfig;
import com.example.lab2.controller.AuthController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.annotation.Resources;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class EmailUtilsTest {

    @Resource(name = "emailUtils")
    EmailUtils emailUtils;

    Logger logger = LoggerFactory.getLogger(EmailUtilsTest.class);

    @Test
    public void testSendEmail() {
        logger.info("日志日志~~");
        try {
            emailUtils.sendEmail("19302010021@fudan.edu.cn", "title", "testtest");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}