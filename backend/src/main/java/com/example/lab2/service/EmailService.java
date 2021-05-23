package com.example.lab2.service;

import com.example.lab2.dao.*;
import com.example.lab2.dto.due.DueBorrowedBookCopyDTO;
import com.example.lab2.dto.due.DueDTO;
import com.example.lab2.dto.due.DueFineDTO;
import com.example.lab2.dto.due.DueReservedBookCopyDTO;
import com.example.lab2.entity.Borrow;
import com.example.lab2.entity.Fine;
import com.example.lab2.entity.Reservation;
import com.example.lab2.entity.User;
import com.example.lab2.exception.auth.NotifyException;
import com.example.lab2.exception.notfound.UserNotFoundException;
import com.example.lab2.utils.EmailUtils;
import com.example.lab2.utils.UserNameAndEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.swing.*;
import java.util.*;

/**
 * @author haojie
 */
@Service("emailService")
public class EmailService {

    @Resource(name = "emailUtils")
    EmailUtils emailUtils;

    @Autowired
    FineRepository fineRepository;

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

    /**
     * @return 成功返回，失败抛出异常
     * @throws MessagingException
     * @author yiwen
     */
    public List<String> sendNotify() throws MessagingException {
        TreeMap<UserNameAndEmail, StringBuilder> usernameEmailToMessage = new TreeMap<>();

        //获得每一个用户所有的预约超期，罚款，及借阅超期
        TreeMap<UserNameAndEmail, List<DueDTO>> userToDueDTO = new TreeMap<>();
        addAllDueReservedBook(userToDueDTO);
        addAllDueBorrowedBook(userToDueDTO);
        addAllDueFineDTO(userToDueDTO);

        //遍历这个map，生成每个用户对应的信息
        for (Map.Entry<UserNameAndEmail, List<DueDTO>> entry : userToDueDTO.entrySet()) {

            UserNameAndEmail userNameAndEmail = entry.getKey();
            List<DueDTO> dueDTOS = entry.getValue();

            //为每个用户生成邮件的头信息
            if (!usernameEmailToMessage.containsKey(userNameAndEmail)) {
                usernameEmailToMessage.put(userNameAndEmail, new StringBuilder(
                        dueDTOS.get(0).getHeadMessage()
                ));
            }

            //对每个用户的过期对象进行遍历，添加这个对象的过期信息
            dueDTOS.forEach((DueDTO dueDTO) -> {
                usernameEmailToMessage.get(userNameAndEmail).append(dueDTO.getDueMessage());
            });

        }

        //显示给前端的信息
        List<String> messageList = new ArrayList<>();
        //为每个用户发邮件
        for (Map.Entry<UserNameAndEmail, StringBuilder> entry : usernameEmailToMessage.entrySet()) {
            UserNameAndEmail userNameAndEmail = entry.getKey();
            String to = userNameAndEmail.email;
            String title = "图书馆的提醒";
            emailUtils.sendEmail(to, title, entry.getValue().toString());
            messageList.add(String.format("向用户%s发邮件成功", userNameAndEmail.username));
        }
        if (messageList.size() == 0) {
            messageList.add("没有可以提醒的用户");
        }

        return messageList;

    }


    /**
     * 把所有的预约超期图书加入到userToDueDTO里面去
     **/
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public void addAllDueReservedBook(TreeMap<UserNameAndEmail, List<DueDTO>> userToDueDTO) {

        //得到所有过期的预约图书
        List<DueReservedBookCopyDTO> dueReservedBookCopyDTOList = bookCopyRepository.getAllDueReservedBookCopyDTO();

        //遍历上面的列表，把对应的过期预约书本记录加入到userToDueDTO里
        dueReservedBookCopyDTOList.forEach((DueReservedBookCopyDTO dueReservedBookCopyDTO) -> {

            //创建用户名和email的对象
            UserNameAndEmail userNameAndEmail = new UserNameAndEmail(
                    dueReservedBookCopyDTO.getUsername(), dueReservedBookCopyDTO.getEmail());

            //把这本预约过期的图书加入到map中
            if (!userToDueDTO.containsKey(userNameAndEmail)) {
                userToDueDTO.put(userNameAndEmail, new ArrayList<>());
            }
            userToDueDTO.get(userNameAndEmail).add(dueReservedBookCopyDTO);

            //取消预约
            reservationRepository.deleteById(dueReservedBookCopyDTO.getReservationID());

        });

    }

    /**
     * 把所有的借阅超期图书加入到userToDueDTO里面去
     *
     * @param userToDueDTO
     */
    public void addAllDueBorrowedBook(TreeMap<UserNameAndEmail, List<DueDTO>> userToDueDTO) {

        //从数据库中获得所有的借阅超期的图书的DTO
        List<DueBorrowedBookCopyDTO> dueBorrowedBookCopyDTOList = bookCopyRepository.getAllDueBorrowedBookCopyDTO();

        //遍历上面的列表，获得map
        dueBorrowedBookCopyDTOList.forEach((DueBorrowedBookCopyDTO dueBorrowedBookCopyDTO) -> {

            //获得username和email的对象
            UserNameAndEmail userNameAndEmail = new UserNameAndEmail(
                    dueBorrowedBookCopyDTO.getUsername(), dueBorrowedBookCopyDTO.getEmail()
            );

            //把这个借阅超期的图书加入到对应的list中去
            if (!userToDueDTO.containsKey(userNameAndEmail)) {
                userToDueDTO.put(userNameAndEmail, new ArrayList<>());
            }
            userToDueDTO.get(userNameAndEmail).add(dueBorrowedBookCopyDTO);

        });

    }

    /**
     * 把所有的没交的罚款加入到userToDueDTO里去
     *
     * @param userToDueDTO
     */
    public void addAllDueFineDTO(TreeMap<UserNameAndEmail, List<DueDTO>> userToDueDTO) {

        //获得所有未支付的fine
        List<DueFineDTO> dueFineDTOList = fineRepository.getAllDueFineDTO();

        //遍历list，获得map
        dueFineDTOList.forEach((DueFineDTO dueFineDTO) -> {
            UserNameAndEmail userNameAndEmail = new UserNameAndEmail(
                    dueFineDTO.getUsername(), dueFineDTO.getEmail()
            );

            if (!userToDueDTO.containsKey(userNameAndEmail)) {
                userToDueDTO.put(userNameAndEmail, new ArrayList<>());
            }
            userToDueDTO.get(userNameAndEmail).add(dueFineDTO);

        });


    }


}


