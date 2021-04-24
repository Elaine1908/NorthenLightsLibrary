package com.example.lab2.service;

import com.example.lab2.dao.*;
import com.example.lab2.entity.*;
import com.example.lab2.exception.bookcopy.BookCopyIsBorrowedException;
import com.example.lab2.exception.bookcopy.BookCopyNotAvailableException;
import com.example.lab2.exception.bookcopy.BookCopyNotHereException;
import com.example.lab2.exception.bookcopy.BookCopyReservedException;
import com.example.lab2.exception.borrow.AdminBorrowBookException;
import com.example.lab2.exception.notfound.BookCopyNotFoundException;
import com.example.lab2.exception.notfound.LibraryNotFoundException;
import com.example.lab2.exception.notfound.UserNotFoundException;
import com.example.lab2.response.GeneralResponse;
import jdk.nashorn.internal.runtime.options.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service("borrowService")
public class BorrowService {


    @Autowired
    UserRepository userRepository;

    @Autowired
    BookCopyRepository bookkCopyRepository;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    LibraryRepository libraryRepository;

    @Autowired
    BorrowRepository borrowRepository;

    /**
     * 管理员给用户借书的service层
     *
     * @param uniqueBookMark 唯一的图书标识
     * @param username       用户名
     * @param adminLibraryID 管理员在哪个图书馆？？
     * @return
     */
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public GeneralResponse lendBookToUser(String uniqueBookMark, String username, Long adminLibraryID) {
        List<Library> libraries = libraryRepository.findAll();


        //检验用户存不存在
        Optional<User> userOptional = userRepository.findByName(username);
        if (!userOptional.isPresent()) {
            throw new UserNotFoundException("用户不存在！");
        }

        //看看用户是不是管理员？如果是管理员的话则不让借书
        if (userOptional.get().getRole().equals(User.ADMIN) || userOptional.get().getRole().equals(User.SUPERADMIN)) {
            throw new AdminBorrowBookException("不能把书借给一个管理员！");
        }

        //检验书的副本存不存在
        Optional<BookCopy> bookCopyOptional = bookkCopyRepository.getBookCopyByUniqueBookMark(uniqueBookMark);
        if (!bookCopyOptional.isPresent()) {
            throw new BookCopyNotFoundException("该图书的副本没有找到！");
        }

        //看看这本书是不是在当前管理员所在的分管？
        if (!bookCopyOptional.get().getLibraryID().equals(adminLibraryID)) {
            //如果不是，提醒用户去对应的分管借书
            libraries.forEach(library -> {
                if (library.getLibraryID() == bookCopyOptional.get().getLibraryID()) {
                    throw new BookCopyNotHereException("这本书不在该管。请去" + library.getLibraryName() + "借书");

                }
            });

            //这本书的libraryid在图书馆列表里找不到？
            throw new LibraryNotFoundException("系统错误，找不到这个图书馆");

        }

        synchronized (BorrowService.class) {

            //看看这本书有没有被预约？
            Optional<Reservation> reservationOptional = reservationRepository.getReservationByBookCopyID(bookCopyOptional.get().getBookCopyID());
            if (reservationOptional.isPresent()) {
                throw new BookCopyReservedException("这本书已经被别人预约了");
            }

            //看看这本书有没有被借走
            Optional<Borrow> borrowOptional = borrowRepository.getBorrowByUniqueBookMark(uniqueBookMark);
            if (borrowOptional.isPresent()) {
                throw new BookCopyIsBorrowedException("这本书已经被别人借走了");
            }

            //看看这本书是否在架上
            if (!bookCopyOptional.get().getStatus().equals(BookCopy.AVAILABLE)) {
                throw new BookCopyNotAvailableException("这本书的状态是" + bookCopyOptional.get().getStatus());
            }

            //如果能运行到这里，说明一切条件都满足了！
            //新建borrow对象
            Date currentDate = new Date();
            Borrow newBorrow = new Borrow(
                    userOptional.get().getUser_id(),
                    uniqueBookMark,
                    currentDate
            );

            //更改原本bookcopy的status属性
            BookCopy bookCopy = bookCopyOptional.get();
            bookCopy.setStatus(BookCopy.BORROWED);
            bookCopy.setLastRentDate(currentDate);

            //更新数据库
            bookkCopyRepository.save(bookCopy);
            borrowRepository.save(newBorrow);

        }

        //返回结果
        return new GeneralResponse("借阅" + uniqueBookMark + "成功！");


    }


}
