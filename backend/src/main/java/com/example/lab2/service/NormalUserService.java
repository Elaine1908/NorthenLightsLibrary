package com.example.lab2.service;

import com.example.lab2.dao.BookCopyRepository;
import com.example.lab2.dao.BorrowRepository;
import com.example.lab2.dao.LibraryRepository;
import com.example.lab2.dao.UserRepository;
import com.example.lab2.dto.BorrowedBookCopyDTO;
import com.example.lab2.dto.ReservedBookCopyDTO;
import com.example.lab2.entity.*;
import com.example.lab2.exception.notfound.UserNotFoundException;
import com.example.lab2.request.borrow.ReturnSingleBookRequest;
import com.example.lab2.response.GeneralResponse;
import com.example.lab2.response.UserInfoResponse;
import com.example.lab2.transaction.returnbook.ReturnBookTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service("normalUserService")
public class NormalUserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookCopyRepository bookCopyRepository;
    @Autowired
    LibraryRepository libraryRepository;
    @Autowired
    BorrowRepository borrowRepository;

    @Autowired
    private AutowireCapableBeanFactory autowireCapableBeanFactory;

    public UserInfoResponse userInfo(String username) {
        User user = userRepository.getUserByUsername(username);
        if (user == null) {
            throw new UserNotFoundException("用户不存在");
        }
        int credit = user.getCredit();
        String role = user.getRole();

        //先根据用户信息创建response对象
        UserInfoResponse userInfoResponse = new UserInfoResponse(username, credit, role);

        //获得用户借阅的所有副本信息
        List<BorrowedBookCopyDTO> borrowedBooks = bookCopyRepository.getBorrowedBookCopiesByUsername(username);

        userInfoResponse.setBorrowedBooks(borrowedBooks);

        //获得用户预约的所有副本信息
        List<ReservedBookCopyDTO> reservedBooks = bookCopyRepository.getAllReservedBooksByUsername(username);

        userInfoResponse.setReservedBooks(reservedBooks);

        return userInfoResponse;


    }

    public List<String> returnBooks(List<ReturnSingleBookRequest> books, Long adminLibraryID, String admin) {

        List<String> stringList = new ArrayList<>();

        //还书成功的数目
        int successfulCount = 0;

        for (ReturnSingleBookRequest returnSingleBookRequest : books) {

            try {
                //尝试还书
                String res = this.returnOnlyOneBook(returnSingleBookRequest, adminLibraryID, admin);

                //如果没有抛出异常，说明成功
                stringList.add(res);

                successfulCount += 1;

            } catch (RuntimeException e) {

                //还书失败，提示用户
                stringList.add(e.getMessage());

            }

        }

        stringList.add(String.format(
                "共成功还掉了%d本图书", successfulCount
        ));

        //返回结果
        return stringList;
    }

    /**
     * 只还一本书的接口。
     *
     * @param returnSingleBookRequest
     * @param adminLibraryID
     * @param admin
     * @return
     * @author zhj
     */
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public String returnOnlyOneBook(
            ReturnSingleBookRequest returnSingleBookRequest,
            Long adminLibraryID, String admin) {

        //初始化状态到类名的哈希表
        HashMap<String, String> statusToClassName = new HashMap<>();
        statusToClassName.put("ok", "com.example.lab2.transaction.returnbook.ReturnNormalBookTransaction");
        statusToClassName.put("damaged", "com.example.lab2.transaction.returnbook.ReturnDamagedBookTransaction");
        statusToClassName.put("lost", "com.example.lab2.transaction.returnbook.ReturnLostBookTransaction");

        try {
            //反射创建对应的transaction
            Class<?> clz = Class.forName(statusToClassName.get(returnSingleBookRequest.getStatus()));
            ReturnBookTransaction returnBookTransaction = (ReturnBookTransaction) clz.getConstructor().newInstance();

            //给反射创建的transaction注入bean
            autowireCapableBeanFactory.autowireBean(returnBookTransaction);

            //尝试还书
            String res = returnBookTransaction.doReturn(returnSingleBookRequest, admin, adminLibraryID);
            return res;
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e.getMessage());
        }


    }


}
