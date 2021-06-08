package com.example.lab2.service;


import com.example.lab2.dao.UserRepository;
import com.example.lab2.entity.User;
import com.example.lab2.exception.notfound.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("superAdminService")
public class SuperAdminService {

    @Autowired
    UserRepository userRepository;

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

        return String.format("设置用户%s的信用到%d成功", username, toWhat);

    }
}
