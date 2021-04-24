package com.example.lab2.service;
import com.example.lab2.dao.BookCopyRepository;
import com.example.lab2.dao.UserRepository;
import com.example.lab2.dto.BorrowedBookCopyDTO;
import com.example.lab2.dto.ReservedBookCopyDTO;
import com.example.lab2.entity.User;
import com.example.lab2.exception.notfound.UserNotFoundException;
import com.example.lab2.response.UserInfoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("normalUserService")
public class NormalUserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookCopyRepository bookCopyRepository;

    public UserInfoResponse userInfo(String username){
        User user = userRepository.getUserByUsername(username);
        if(user == null){
            throw new UserNotFoundException("用户不存在");
        }
        int credit = user.getCredit();
        String role = user.getRole();

        //先根据用户信息创建response对象
        UserInfoResponse userInfoResponse = new UserInfoResponse(username,credit,role);

        //获得用户借阅的所有副本信息
        List<BorrowedBookCopyDTO> borrowedBooks = bookCopyRepository.getBorrowedBookCopiesByUsername(username);

        userInfoResponse.setBorrowedBooks(borrowedBooks);

        //获得用户预约的所有副本信息
        List<ReservedBookCopyDTO> reservedBooks = bookCopyRepository.getAllReservedBooksByUsername(username);

        userInfoResponse.setReservedBooks(reservedBooks);

        return userInfoResponse;


    }

}
