package com.example.lab2.service;

import com.example.lab2.dao.FineRepository;
import com.example.lab2.dao.UserRepository;
import com.example.lab2.entity.Fine;
import com.example.lab2.entity.User;
import com.example.lab2.exception.notfound.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("fineService")
public class FineService {


    @Autowired
    FineRepository fineRepository;

    @Autowired
    UserRepository userRepository;

    /**
     * @param username 用户名
     * @return
     * @author zhj
     * 根据用户名，获得用户的全部罚款
     */
    public List<Fine> getAllFine(String username) {
        //先根据用户名查找用户，找出userID
        Optional<User> userOptional = userRepository.findByName(username);
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("找不到用户");
        }
        long userID = userOptional.get().getUser_id();

        //根据userID查罚款
        List<Fine> fineList = fineRepository.getFineByUserID(userID);

        return fineList;
    }

}
