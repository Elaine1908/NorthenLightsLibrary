package com.example.lab2.service;


import com.example.lab2.dao.UserRepository;
import com.example.lab2.dao.record.CreditRecordRepository;
import com.example.lab2.entity.CreditRecord;
import com.example.lab2.entity.User;
import com.example.lab2.exception.notfound.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service("superAdminService")
public class SuperAdminService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    CreditRecordRepository creditRecordRepository;

    /**
     * 管理员重置用户的信用到一个指定的数值
     *
     * @param username 用户名
     * @param toWhat   要将用户的信用重设到多少
     * @return 信息
     */
    public String resetCredit(String username, int toWhat) {

        //从数据库中得到用户，并检查用户是否存在
        User user = userRepository.getUserByUsername(username);
        if (user == null) {
            throw new UserNotFoundException("找不到用户" + username);
        }

        //设置用户的信用到对应的数值
        user.setCredit(toWhat);

        //在数据库中保存
        userRepository.save(user);

        //添加信用记录
        CreditRecord creditRecord = new CreditRecord(
                user.getUser_id(),
                toWhat,
                String.format("管理员将您的信用重置为%d", toWhat),
                new Date()
        );

        creditRecordRepository.save(creditRecord);

        return String.format("设置用户%s的信用到%d成功", username, toWhat);

    }
}
