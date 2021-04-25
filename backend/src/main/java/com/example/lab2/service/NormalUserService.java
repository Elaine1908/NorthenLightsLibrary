package com.example.lab2.service;

import com.example.lab2.dao.BookCopyRepository;
import com.example.lab2.dao.BorrowRepository;
import com.example.lab2.dao.LibraryRepository;
import com.example.lab2.dao.UserRepository;
import com.example.lab2.dto.BorrowedBookCopyDTO;
import com.example.lab2.dto.ReservedBookCopyDTO;
import com.example.lab2.entity.*;
import com.example.lab2.exception.bookcopy.BookCopyNotAvailableException;
import com.example.lab2.exception.bookcopy.BookCopyNotHereException;
import com.example.lab2.exception.borrow.NotBorrowedException;
import com.example.lab2.exception.notfound.BookCopyNotFoundException;
import com.example.lab2.exception.notfound.LibraryNotFoundException;
import com.example.lab2.exception.notfound.UserNotFoundException;
import com.example.lab2.exception.reserve.NotReservedException;
import com.example.lab2.exception.reserve.ReservedByOtherException;
import com.example.lab2.response.GeneralResponse;
import com.example.lab2.response.UserInfoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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

    public GeneralResponse returnBooks(List<String> books, Long adminLibraryID, String admin) {

        StringBuilder messageBuilder = new StringBuilder();

        //还书成功的数目
        int successfulCount = 0;

        for (String uniqueBookMark : books) {

            try {
                //尝试还书
                this.returnOnlyOneBook(uniqueBookMark, adminLibraryID, admin);

                //如果没有抛出异常，说明成功
                messageBuilder.append(String.format("还书%s成功;", uniqueBookMark));

                successfulCount += 1;

            } catch (RuntimeException e) {

                //还书失败，提示用户
                messageBuilder.append(e.getMessage()).append(";");

            }

        }

        messageBuilder.append("共成功还掉了").append(successfulCount).append("本图书;");

        //返回结果
        return new GeneralResponse(messageBuilder.toString());
    }

    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void returnOnlyOneBook(
            String uniqueBookMark,
            Long adminLibraryID, String admin) {

        //看看副本存不存在
        Optional<BookCopy> bookCopyOptional = bookCopyRepository.getBookCopyByUniqueBookMark(uniqueBookMark);
        if (!bookCopyOptional.isPresent()) {
            throw new BookCopyNotFoundException(uniqueBookMark + " 的副本没有找到！");
        }

        //看看这本书是否是借出状态
        Optional<Borrow> borrowOptional = borrowRepository.getBorrowByUniqueBookMark(uniqueBookMark);
        if (!bookCopyOptional.get().getStatus().equals(BookCopy.BORROWED) || !borrowOptional.isPresent()) {
            throw new NotBorrowedException("书本 " + uniqueBookMark + " 当前未被借阅");
        }

        //看看图书馆存不存在
        Optional<Library> libraryOptional = libraryRepository.findById(adminLibraryID);
        if (!libraryOptional.isPresent()) {
            throw new LibraryNotFoundException("图书馆未找到，请检查参数是否正确");
        }

        //得到管理员的ID
        Optional<User> adminOptional = userRepository.findByName(admin);
        if (!adminOptional.isPresent()) {
            throw new UserNotFoundException("找不到管理员" + admin);
        }
        Long adminID = adminOptional.get().getUser_id();


        Date currentDate = new Date();
        //更改原本bookcopy的status属性
        BookCopy bookCopy = bookCopyOptional.get();
        bookCopy.setStatus(BookCopy.AVAILABLE);
        bookCopy.setLastReturnDate(currentDate);
        bookCopy.setAdminID(adminID);
        bookCopy.setLibraryID(adminLibraryID);

        //删除借阅条目
        borrowRepository.delete(borrowOptional.get());
        //更新副本状态
        bookCopyRepository.save(bookCopy);

    }


}
