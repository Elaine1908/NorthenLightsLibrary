package com.example.lab2.service;

import com.example.lab2.utils.EmailUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.util.UUID;

/**
 * @author haojie
 */
@Service("emailService")
public class EmailService {

    @Resource(name = "emailUtils")
    EmailUtils emailUtils;

    /**
     * 用户注册时，给用户的邮箱发送验证码的业务
     *
     * @param to 用户的邮箱
     * @return 发送到用户邮箱的验证码
     * @author haojie
     */
    public String sendRegisterCaptcha(String to) throws MessagingException {
        String captcha = UUID.randomUUID().toString();
        String text = String.format("你的验证码是%s", captcha);
        emailUtils.sendEmail(to, "图书馆注册验证码", text);
        return captcha;

    }


}
