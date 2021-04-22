package com.example.lab2.service;

import com.example.lab2.dao.*;
import com.example.lab2.dao.UserRepository;
import com.example.lab2.entity.BookCopy;
import com.example.lab2.entity.Reservation;
import com.example.lab2.entity.User;
import com.example.lab2.exception.ReserveException;
import com.example.lab2.response.GeneralResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service("reserveService")
public class ReserveService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    BookkCopyRepository bookkCopyRepository;


    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public GeneralResponse reserveBook(String uniqueBookMark, String username) {

        //根据用户名得到正在预约的用户
        Optional<User> userReserving = userRepository.findByName(username);

        //如果找不到用户就抛出异常显示用户不存在
        if (!userReserving.isPresent()) {
            throw new ReserveException("用户不存在！");
        }

        //在数据库中找找看这个副本
        Optional<BookCopy> desiredBookCopy = bookkCopyRepository.getBookCopyByUniqueBookMark(uniqueBookMark);

        //如果找不到这个副本就提示不存在！
        if (!desiredBookCopy.isPresent()) {
            throw new ReserveException("该副本不存在！");
        }

        //看看用户已经预约了多少本图书
        long cnt = reservationRepository.getReservationCountByUserID(userReserving.get().getUser_id());
        if (cnt >= 10) {
            throw new ReserveException("系统规定一个用户最多能借阅10本图书，你已经借阅了" + cnt + "本");
        }

        //加锁，防止出现两个人抢着预约到了同一本书的情况
        synchronized (ReserveService.class) {
            //看看这本书是否已经被预约了
            Optional<Reservation> reservation = reservationRepository.getReservationByBookCopyID(desiredBookCopy.get().getBookCopyID());
            //如果已经被预约了就提示错误
            if (reservation.isPresent()) {
                throw new ReserveException("这本书已经被预约了，不能再预约了");
            }
            //如果这个副本不可用或是损毁了或是丢失了，就提示用户对应的信息
            if (!desiredBookCopy.get().isAvailable()) {
                throw new ReserveException("这本书的状态是:" + desiredBookCopy.get().getStatus() + ",因此不可借用");
            }

            //更新书本副本的状态为已预约，设置最近一次借阅时间
            BookCopy bc = desiredBookCopy.get();
            bc.setStatus(BookCopy.RESERVED);
            bc.setLastRentDate(new Date());
            bookkCopyRepository.save(bc);

            //在预约表里插入一个新的预约
            Reservation newReservation = new Reservation(
                    userReserving.get().getUser_id(), bc.getBookCopyID()
            );
            reservationRepository.save(newReservation);


        }

        return new GeneralResponse("预约" + desiredBookCopy.get().getUniqueBookMark() + "成功！");

    }
}
