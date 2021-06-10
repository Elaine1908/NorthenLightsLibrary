package com.example.lab2.service;

import com.example.lab2.dao.*;
import com.example.lab2.dao.UserRepository;
import com.example.lab2.dao.record.ReserveRecordRepository;
import com.example.lab2.dto.bookcopy.ReservedBookCopyDTO;
import com.example.lab2.entity.*;
import com.example.lab2.exception.auth.RoleNotAllowedException;
import com.example.lab2.exception.bookcopy.BookCopyNotAvailableException;
import com.example.lab2.exception.notfound.BookCopyNotFoundException;
import com.example.lab2.exception.notfound.UserNotFoundException;
import com.example.lab2.exception.reserve.AdminReserveBookException;
import com.example.lab2.exception.reserve.ReserveTooManyException;
import com.example.lab2.exception.reserve.ReservedByOtherException;
import com.example.lab2.response.GeneralResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service("reserveService")
public class ReserveService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    BookCopyRepository bookkCopyRepository;

    @Autowired
    UserConfigurationRepository userConfigurationRepository;

    @Autowired
    ReserveRecordRepository reserveRecordRepository;

    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public GeneralResponse reserveBook(String uniqueBookMark, String username) {

        //根据用户名得到正在预约的用户
        Optional<User> userReserving = userRepository.findByName(username);

        //如果找不到用户就抛出异常显示用户不存在
        if (!userReserving.isPresent()) {
            throw new UserNotFoundException("用户不存在！");
        }

        //管理员不能预约图书
        if (userReserving.orElse(null).isAdmin()) {
            throw new AdminReserveBookException("管理员不能借阅图书");
        }

        //得到当前用户的UserConfiguration,里面包含了最长预约时间
        Optional<UserConfiguration> userConfiguration = userConfigurationRepository.findUserConfigurationByRole(
                userReserving.orElse(null).getRole());
        if (userConfiguration.isEmpty()) {
            throw new RoleNotAllowedException("用户角色错误");
        }

        //在数据库中找找看这个副本
        Optional<BookCopy> desiredBookCopy = bookkCopyRepository.getBookCopyByUniqueBookMark(uniqueBookMark);

        //如果找不到这个副本就提示不存在！
        if (!desiredBookCopy.isPresent()) {
            throw new BookCopyNotFoundException("该副本不存在！");
        }

        //看看用户已经预约了多少本图书
        long cnt = reservationRepository.getReservationCountByUserID(userReserving.orElse(null).getUser_id());
        if (cnt >= 10) {
            throw new ReserveTooManyException("系统规定一个用户最多能预约10本图书，你已经预约了" + cnt + "本");
        }

        //加锁，防止出现两个人抢着预约到了同一本书的情况
        synchronized (ReserveService.class) {
            //看看这本书是否已经被预约了
            Optional<Reservation> reservation = reservationRepository.getReservationByBookCopyID(desiredBookCopy.orElse(null).getBookCopyID());
            //如果已经被预约了就提示错误
            if (reservation.isPresent()) {
                throw new ReservedByOtherException("这本书已经被预约了，不能再预约了");
            }
            //如果这个副本不可用或是损毁了或是丢失了，就提示用户对应的信息
            if (!desiredBookCopy.orElse(null).isAvailable()) {
                throw new BookCopyNotAvailableException("这本书的状态是:" + desiredBookCopy.orElse(null).getStatus() + ",因此不可借用");
            }

            //更新书本副本的状态为已预约，设置最近一次借阅时间
            Date currentDate = new Date();
            BookCopy bc = desiredBookCopy.orElse(null);
            bc.setStatus(BookCopy.RESERVED);
            bc.setLastReservationDate(currentDate);
            bookkCopyRepository.save(bc);

            Date deadLine = new Date(currentDate.getTime() + 1000 * userConfiguration.orElse(null).getMaxReserveTime());

            //在预约表里插入一个新的预约
            Reservation newReservation = new Reservation(
                    userReserving.orElse(null).getUser_id(), bc.getBookCopyID(), currentDate, deadLine
            );
            reservationRepository.save(newReservation);

            //在预约记录表中插入新的预约记录
            ReserveRecord reserveRecord = new ReserveRecord(userReserving.orElse(null).getUser_id(),currentDate,bc.getUniqueBookMark(),"admin",1);
            //其实在预约记录中不需要记录admin和libraryID，但是这里为了后续数据库查找的方便默认将所有的admin和libraryID统一设置成了admin和0
            reserveRecordRepository.save(reserveRecord);


        }

        return new GeneralResponse("预约" + desiredBookCopy.orElse(null).getUniqueBookMark() + "成功！");

    }

    /**
     * 根据用户名，获得他预约过的所有书籍的函数
     *
     * @param username
     * @return
     */
    public List<ReservedBookCopyDTO> getAllReservation(String username) {

        //看看用户存不存在
        Optional<User> userOptional = userRepository.findByName(username);
        if (!userOptional.isPresent()) {
            throw new UserNotFoundException("找不到这个用户！");
        }


        return bookkCopyRepository.getAllReservedBooksByUsername(username);
    }
}
