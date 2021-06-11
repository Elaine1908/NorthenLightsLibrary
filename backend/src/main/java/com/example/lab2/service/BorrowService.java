package com.example.lab2.service;

import com.example.lab2.dao.*;
import com.example.lab2.dao.record.BorrowRecordRepository;
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
import com.example.lab2.exception.notfound.UserTypeNotFoundException;
import com.example.lab2.exception.reserve.NotReservedException;
import com.example.lab2.exception.reserve.ReservationDueException;
import com.example.lab2.exception.reserve.ReservedByOtherException;
import com.example.lab2.response.GeneralResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Locale;
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

    @Autowired
    BorrowRecordRepository borrowRecordRepository;

    /**
     * 管理员给用户借书的service层
     *
     * @param uniqueBookMarkList 唯一的图书标识
     * @param username           用户名
     * @param adminLibraryID     管理员在哪个图书馆？？
     * @return
     */
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public GeneralResponse lendBookToUser(List<String> uniqueBookMarkList,
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

        int successfulCnt = 0;
        StringBuilder messageBuilder = new StringBuilder();

        //尝试把书一本本借阅给用户
        for (String uniqueBookMark : uniqueBookMarkList
        ) {
            try {
                //尝试把这本书借给用户
                this.lendOnlyOneBookToUser(
                        uniqueBookMark,
                        adminLibraryID,
                        libraries,
                        userOptional.get(),
                        admin,
                        userConfigurationOptional
                );

                //如果能运行到这里，说明借阅成功
                successfulCnt += 1;

                //添加借阅成功的信息
                messageBuilder.append(String.format("借阅%s成功;", uniqueBookMark));

            } catch (RuntimeException e) {
                messageBuilder.append(e.getMessage()).append(";");
            }
        }

        //添加最后借阅成功了多少图书
        messageBuilder.append(String.format("共成功借阅了%d本图书。", successfulCnt));

        //返回结果
        return new GeneralResponse(messageBuilder.toString());


    }


    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void lendOnlyOneBookToUser(String uniqueBookMark,
                                      Long adminLibraryID,
                                      List<Library> libraries,
                                      User user, String admin,
                                      Optional<UserConfiguration> userConfigurationOptional) {

        //检验书的副本存不存在
        Optional<BookCopy> bookCopyOptional = bookkCopyRepository.getBookCopyByUniqueBookMark(uniqueBookMark);
        if (bookCopyOptional.isEmpty()) {
            throw new BookCopyNotFoundException(uniqueBookMark + "的副本没有找到！");
        }

        //看看这本书是不是在当前管理员所在的分管？
        if (!bookCopyOptional.orElse(null).getLibraryID().equals(adminLibraryID)) {
            //如果不是，提醒用户去对应的分管借书
            libraries.forEach(library -> {
                if (library.getLibraryID() == bookCopyOptional.get().getLibraryID()) {
                    throw new BookCopyNotHereException(uniqueBookMark + "不在该馆。请去" + library.getLibraryName() + "借书");

                }
            });

            //这本书的libraryid在图书馆列表里找不到？
            throw new LibraryNotFoundException("系统错误，找不到这个图书馆");

        }

        if (userConfigurationOptional.isEmpty()) {
            throw new UserTypeNotFoundException("用户角色错误");
        }

        //检查用户是否借阅了太多书,如果是的话给出提示
        long currentBorrowCount = borrowRepository.getBorrowCountByUsername(user.getUsername());
        long currentReservationCount = reservationRepository.getReservationCountByUsername(user.getUsername());

        if (currentBorrowCount + currentReservationCount >= userConfigurationOptional.orElse(null).getMaxBookBorrow()) {
            throw new BorrowToManyException(
                    String.format("您系统设置您最大可以借阅%d本书，你已经借阅了%d本书，不能再借阅了"
                            , userConfigurationOptional.orElse(null).getMaxBookBorrow(), currentBorrowCount)
            );
        }


        synchronized (BorrowService.class) {

            //看看这本书有没有被预约？
            Optional<Reservation> reservationOptional = reservationRepository.getReservationByBookCopyID(bookCopyOptional.orElse(null).getBookCopyID());
            if (reservationOptional.isPresent()) {
                throw new BookCopyReservedException(uniqueBookMark + "已经被别人预约了");
            }

            //看看这本书有没有被借走
            Optional<Borrow> borrowOptional = borrowRepository.getBorrowByUniqueBookMark(uniqueBookMark);
            if (borrowOptional.isPresent()) {
                throw new BookCopyIsBorrowedException(uniqueBookMark + "已经被别人借走了");
            }

            //看看这本书是否在架上
            if (!bookCopyOptional.orElse(null).getStatus().equals(BookCopy.AVAILABLE)) {
                throw new BookCopyNotAvailableException(uniqueBookMark + "的状态是" + bookCopyOptional.orElse(null).getStatus());
            }

            //如果能运行到这里，说明一切条件都满足了！
            //新建borrow对象
            Date currentDate = new Date();
            Date deadline = new Date(currentDate.getTime() + 1000 * userConfigurationOptional.orElse(null).getMaxBorrowTime());
            Borrow newBorrow = new Borrow(
                    user.getUser_id(),
                    uniqueBookMark,
                    currentDate,
                    deadline
            );

            //得到管理员的ID
            Optional<User> adminOptional = userRepository.findByName(admin);
            if (adminOptional.isEmpty()) {
                throw new UserNotFoundException("找不到管理员" + admin);
            }
            Long adminID = adminOptional.orElse(null).getUser_id();


            //更改原本bookcopy的status属性
            BookCopy bookCopy = bookCopyOptional.orElse(null);
            bookCopy.setStatus(BookCopy.BORROWED);
            bookCopy.setLastRentDate(currentDate);
            bookCopy.setAdminID(adminID);
            bookCopy.setBorrower(user.getUsername());
            bookCopy.setLibraryID(adminLibraryID);
            //借出的分馆？？

            //更新数据库
            bookkCopyRepository.save(bookCopy);
            borrowRepository.save(newBorrow);

            //新建借阅记录
            BorrowRecord borrowRecord = new BorrowRecord(user.getUser_id(), currentDate, uniqueBookMark, admin, adminLibraryID);
            borrowRecordRepository.save(borrowRecord);

        }


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
                this.lendOnlyOneReservedBookToUser(uniqueBookMark, adminLibraryID, libraries, userOptional.orElse(null), admin);

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

        //看看预约是否超期
        Date currentDate = new Date();
        Date reservationDeadline = reservationOptional.orElse(null).getDeadline();
        if (reservationDeadline != null && reservationDeadline.getTime() < currentDate.getTime()) {
            throw new ReservationDueException(String.format("此预约已与%s到期，不能取书", reservationDeadline.toString()));
        }

        //获得这个用户的最大借阅时间和最多能借多少本书
        Optional<UserConfiguration> userConfigurationOptional = userConfigurationRepository.findUserConfigurationByRole(user.getRole());
        if (userConfigurationOptional.isEmpty()) {
            throw new RoleNotAllowedException("角色错误");
        }


        //看看这本书是不是这个人预约的
        if (!reservationOptional.orElse(null).getUserID().equals(user.getUser_id())) {
            throw new ReservedByOtherException(uniqueBookMark + "不是" + user.getUsername() + "预约的");
        }


        //看看这本书是不是在当前管理员所在的分馆
        if (!bookCopyOptional.orElse(null).getLibraryID().equals(adminLibraryID)) {
            //如果不是，提醒用户去对应的分管借书
            libraries.forEach(library -> {
                if (library.getLibraryID() == bookCopyOptional.orElse(null).getLibraryID()) {
                    throw new BookCopyNotHereException(uniqueBookMark + "不在该管。请去" + library.getLibraryName() + "借书");
                }
            });

            //这本书的libraryid在图书馆列表里找不到？
            throw new LibraryNotFoundException("系统错误，找不到这个图书馆");
        }

        //看看这本书是否在架上
        if (!bookCopyOptional.orElse(null).getStatus().equals(BookCopy.AVAILABLE)) {
            if (!bookCopyOptional.orElse(null).getStatus().equals(BookCopy.RESERVED)) {
                throw new BookCopyNotAvailableException(uniqueBookMark + "的状态是" + bookCopyOptional.orElse(null).getStatus());
            }
        }

        //看看用户是不是借阅了太多书
        long currentBorrowCount = borrowRepository.getBorrowCountByUsername(user.getUsername());
        long currentReserveCount = reservationRepository.getReservationCountByUsername(user.getUsername());

        if (currentReserveCount + currentBorrowCount >= userConfigurationOptional.orElse(null).getMaxBookBorrow()) {
            throw new BorrowToManyException(
                    String.format("您系统设置您最大可以借阅%d本书，你已经借阅了%d本书，不能再借阅了"
                            , userConfigurationOptional.orElse(null).getMaxBookBorrow(), currentBorrowCount)
            );
        }

        //新建borrow对象
        Date deadline = new Date(currentDate.getTime() + userConfigurationOptional.orElse(null).getMaxBorrowTime() * 1000);
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
        Long adminID = adminOptional.orElse(null).getUser_id();

        //更改原本bookcopy的status属性
        BookCopy bookCopy = bookCopyOptional.orElse(null);
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

        //新建借阅记录
        BorrowRecord borrowRecord = new BorrowRecord(user.getUser_id(), currentDate, uniqueBookMark, admin, adminLibraryID);
        borrowRecordRepository.save(borrowRecord);

    }

}
