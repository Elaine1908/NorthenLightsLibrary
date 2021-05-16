package com.example.lab2.service;

import com.example.lab2.dao.*;
import com.example.lab2.entity.*;
import com.example.lab2.exception.auth.RoleNotAllowedException;
import com.example.lab2.exception.bookcopy.BookCopyIsBorrowedException;
import com.example.lab2.exception.bookcopy.BookCopyNotAvailableException;
import com.example.lab2.exception.bookcopy.BookCopyNotHereException;
import com.example.lab2.exception.bookcopy.BookCopyReservedException;
import com.example.lab2.exception.borrow.AdminBorrowBookException;
import com.example.lab2.exception.borrow.BorrowToManyException;
import com.example.lab2.exception.notfound.BookCopyNotFoundException;
import com.example.lab2.exception.notfound.LibraryNotFoundException;
import com.example.lab2.exception.notfound.UserNotFoundException;
import com.example.lab2.exception.reserve.NotReservedException;
import com.example.lab2.exception.reserve.ReservedByOtherException;
import com.example.lab2.response.GeneralResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
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

    @Autowired
    UserConfigurationRepository userConfigurationRepository;

    /**
     * 管理员给用户借书的service层
     *
     * @param uniqueBookMark 唯一的图书标识
     * @param username       用户名
     * @param adminLibraryID 管理员在哪个图书馆？？
     * @return
     */
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public GeneralResponse lendBookToUser(String uniqueBookMark,
                                          String username,
                                          Long adminLibraryID,
                                          String admin) {
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

        //获得用户最多能借阅多长时间
        Optional<UserConfiguration> userConfigurationOptional = userConfigurationRepository.findUserConfigurationByRole(
                userOptional.get().getRole()
        );

        if (userConfigurationOptional.isEmpty()) {
            throw new RoleNotAllowedException("用户角色错误");
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

        //检查用户是否借阅了太多书,如果是的话给出提示
        long currentBorrowCount = borrowRepository.getBorrowCountByUsername(userOptional.get().getUsername());
        if (currentBorrowCount >= userConfigurationOptional.get().getMaxBookBorrow()) {
            throw new BorrowToManyException(
                    String.format("您系统设置您最大可以借阅%d本书，你已经借阅了%d本书，不能再借阅了"
                            , userConfigurationOptional.get().getMaxBookBorrow(), currentBorrowCount)
            );
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
            Date deadline = new Date(currentDate.getTime() + 1000 * userConfigurationOptional.get().getMaxBorrowTime());
            Borrow newBorrow = new Borrow(
                    userOptional.get().getUser_id(),
                    uniqueBookMark,
                    currentDate,
                    deadline
            );

            //得到管理员的ID
            Optional<User> adminOptional = userRepository.findByName(admin);
            if (!adminOptional.isPresent()) {
                throw new UserNotFoundException("找不到管理员" + admin);
            }
            Long adminID = adminOptional.get().getUser_id();


            //更改原本bookcopy的status属性
            BookCopy bookCopy = bookCopyOptional.get();
            bookCopy.setStatus(BookCopy.BORROWED);
            bookCopy.setLastRentDate(currentDate);
            bookCopy.setAdminID(adminID);
            bookCopy.setBorrower(username);
            bookCopy.setLibraryID(adminLibraryID);
            //借出的分馆？？

            //更新数据库
            bookkCopyRepository.save(bookCopy);
            borrowRepository.save(newBorrow);

        }

        //返回结果
        return new GeneralResponse("借阅" + uniqueBookMark + "成功！");


    }

    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public GeneralResponse lendReservedBookToUser(String username,
                                                  List<String> booklist,
                                                  Long adminLibraryID,
                                                  String admin) {
        List<Library> libraries = libraryRepository.findAll();
        //检验用户存不存在
        Optional<User> userOptional = userRepository.findByName(username);
        if (!userOptional.isPresent()) {
            throw new UserNotFoundException("用户不存在！");
        }
        if (userOptional.get().isAdmin()) {
            throw new AdminBorrowBookException("管理员不能借书！");
        }

        StringBuilder messageBuilder = new StringBuilder();

        //取预约的图书的成功的数目
        int successfulCount = 0;

        for (String uniqueBookMark : booklist) {

            try {
                //尝试把这本预约的书借给用户
                this.lendOnlyOneReservedBookToUser(uniqueBookMark, adminLibraryID, libraries, userOptional.get(), admin);

                //如果没有抛出异常，说明成功
                messageBuilder.append(String.format("取预约的书%s成功;", uniqueBookMark));

                successfulCount += 1;

            } catch (RuntimeException e) {

                //借阅失败，提示用户
                messageBuilder.append(e.getMessage()).append(";");

            }

        }

        messageBuilder.append("共成功借走了").append(successfulCount).append("本图书;");

        //返回结果
        return new GeneralResponse(messageBuilder.toString());

    }

    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void lendOnlyOneReservedBookToUser(
            String uniqueBookMark,
            Long adminLibraryID,
            List<Library> libraries,
            User user, String admin) {

        //看看这本bookcopy存不存在
        Optional<BookCopy> bookCopyOptional = bookkCopyRepository.getBookCopyByUniqueBookMark(uniqueBookMark);
        if (!bookCopyOptional.isPresent()) {
            throw new BookCopyNotFoundException(uniqueBookMark + "的副本没有找到！");
        }

        //看看这本书有没有预约
        Optional<Reservation> reservationOptional = reservationRepository.getReservationByBookCopyID(bookCopyOptional.get().getBookCopyID());
        if (!reservationOptional.isPresent()) {
            throw new NotReservedException(uniqueBookMark + "还没有被预约");
        }

        //获得这个用户的最大借阅时间和最多能借多少本书
        Optional<UserConfiguration> userConfiguration = userConfigurationRepository.findUserConfigurationByRole(user.getRole());
        if (userConfiguration.isEmpty()) {
            throw new RoleNotAllowedException("角色错误");
        }


        //看看这本书是不是这个人预约的
        if (!reservationOptional.get().getUserID().equals(user.getUser_id())) {
            throw new ReservedByOtherException(uniqueBookMark + "不是" + user.getUsername() + "预约的");
        }


        //看看这本书是不是在当前管理员所在的分馆
        if (!bookCopyOptional.get().getLibraryID().equals(adminLibraryID)) {
            //如果不是，提醒用户去对应的分管借书
            libraries.forEach(library -> {
                if (library.getLibraryID() == bookCopyOptional.get().getLibraryID()) {
                    throw new BookCopyNotHereException(uniqueBookMark + "不在该管。请去" + library.getLibraryName() + "借书");
                }
            });

            //这本书的libraryid在图书馆列表里找不到？
            throw new LibraryNotFoundException("系统错误，找不到这个图书馆");
        }

        //看看这本书是否在架上
        if (!bookCopyOptional.get().getStatus().equals(BookCopy.AVAILABLE)) {
            if (!bookCopyOptional.get().getStatus().equals(BookCopy.RESERVED)) {
                throw new BookCopyNotAvailableException(uniqueBookMark + "的状态是" + bookCopyOptional.get().getStatus());
            }
        }


        //新建borrow对象
        Date currentDate = new Date();
        Date deadline = new Date(currentDate.getTime() + userConfiguration.get().getMaxBorrowTime() * 1000);
        Borrow newBorrow = new Borrow(
                user.getUser_id(),
                uniqueBookMark,
                currentDate,
                deadline
        );

        //得到管理员的ID
        Optional<User> adminOptional = userRepository.findByName(admin);
        if (!adminOptional.isPresent()) {
            throw new UserNotFoundException("找不到管理员" + admin);
        }
        Long adminID = adminOptional.get().getUser_id();

        //更改原本bookcopy的status属性
        BookCopy bookCopy = bookCopyOptional.get();
        bookCopy.setStatus(BookCopy.BORROWED);
        bookCopy.setLastRentDate(currentDate);
        bookCopy.setAdminID(adminID);
        bookCopy.setBorrower(user.getUsername());
        bookCopy.setLibraryID(adminLibraryID);

        //删除预约条目
        reservationRepository.deleteReservationByBookCopyID(bookCopy.getBookCopyID());
        //更新副本状态
        bookkCopyRepository.save(bookCopy);
        //新建借阅条目
        borrowRepository.save(newBorrow);

    }

}
