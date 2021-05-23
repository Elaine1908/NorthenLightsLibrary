package com.example.lab2.utils;

import com.example.lab2.config.EmailConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * @author haojie
 */
@Service("emailUtils")
public class EmailUtils {

    @Autowired
    private EmailConfig emailConfig;


    /**
     * 给用户发邮件的函数
     *
     * @param to    用户的邮箱
     * @param title 邮件的标题
     * @param text  邮件的body
     * @throws MessagingException 异常？
     * @author haojie
     */
    public void sendEmail(String to, String title, String text) throws MessagingException {
        String smtp = emailConfig.getSmtp_server();
        String username = emailConfig.getUsername();
        String password = emailConfig.getPassword();

        Properties props = new Properties();
        props.put("mail.smtp.host", smtp); // SMTP主机名
        props.put("mail.smtp.auth", "true"); // 是否需要用户认证
        props.setProperty("mail.smtp.port", "465");
        props.setProperty("mail.transport.protocol", "SMTP"); // 设置邮箱发送的协议
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.starttls.enable", "true"); // 启用TLS加密
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
// 获取Session实例:
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        MimeMessage message = new MimeMessage(session);
// 设置发送方地址:
        message.setFrom(new InternetAddress(username));
// 设置接收方地址:
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
// 设置邮件主题:
        message.setSubject(title, "UTF-8");
// 设置邮件正文:
        message.setText(text, "UTF-8");
// 发送:
        Transport.send(message);

    }

}
