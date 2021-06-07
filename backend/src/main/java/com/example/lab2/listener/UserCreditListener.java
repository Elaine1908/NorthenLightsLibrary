package com.example.lab2.listener;

import com.example.lab2.dao.UserRepository;
import com.example.lab2.dao.record.CreditRecordRepository;
import com.example.lab2.entity.CreditRecord;
import com.example.lab2.entity.User;
import com.example.lab2.exception.notfound.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service("userCreditListener")
public class UserCreditListener {

    @Autowired
    UserRepository userRepository;

    @Autowired
    CreditRecordRepository creditRecordRepository;

    public void decreaseUserCredit(String username, String reason, int decreaseAmount) {
        User user = userRepository.getUserByUsername(username);
        if (user == null) {
            return;
        }

        //降低用户的信用
        user.setCredit(user.getCredit() - decreaseAmount);

        //保存到数据库
        userRepository.save(user);

        //创建信誉记录
        CreditRecord creditRecord = new CreditRecord(
                user.getUser_id(),
                decreaseAmount,
                reason,
                new Date()
        );

        //将信誉记录存储
        creditRecordRepository.save(creditRecord);


    }
}
