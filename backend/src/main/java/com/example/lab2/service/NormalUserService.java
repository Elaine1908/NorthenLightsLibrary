package com.example.lab2.service;
import com.example.lab2.dao.BookCopyRepository;
import com.example.lab2.dao.BookTypeRepository;
import com.example.lab2.dao.UserRepository;
import com.example.lab2.dto.BookDTO;
import com.example.lab2.entity.User;
import com.example.lab2.exception.LoginException;
import com.example.lab2.exception.notfound.UserNotFoundException;
import com.example.lab2.response.GeneralResponse;
import com.example.lab2.response.UserInfoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.util.List;

@Service
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
        long user_id = user.getUser_id();
        int credit = user.getCredit();
        String role = user.getRole();

        //先根据用户信息创建response对象
        UserInfoResponse userInfoResponse = new UserInfoResponse(username,credit,role);
        //获得用户借阅的所有副本信息
        List<BookDTO> borrowedBooks = bookCopyRepository.getBorrowedBookCopiesByUsername(username);

        userInfoResponse.setBorrowedBooks(borrowedBooks);

        //获得用户预约的所有副本信息
        List<BookDTO> reservedBooks = bookCopyRepository.getReservedBookCopiesByUsername(username);

        userInfoResponse.setBorrowedBooks(reservedBooks);

        return userInfoResponse;
    }

    public GeneralResponse showReservation(String username){
        if(userRepository.getUserByUsername(username) == null){
            throw new UserNotFoundException("用户不存在");
        }
return new GeneralResponse("");
    }
}
