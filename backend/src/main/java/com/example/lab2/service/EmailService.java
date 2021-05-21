package com.example.lab2.service;

import com.example.lab2.dao.*;
import com.example.lab2.entity.Borrow;
import com.example.lab2.entity.Fine;
import com.example.lab2.entity.Reservation;
import com.example.lab2.entity.User;
import com.example.lab2.exception.auth.NotifyException;
import com.example.lab2.exception.notfound.UserNotFoundException;
import com.example.lab2.utils.EmailUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.util.*;

/**
 * @author haojie
 */
@Service("emailService")
public class EmailService {

    @Resource(name = "emailUtils")
    EmailUtils emailUtils;

    @Autowired
    FineRepository fineRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    BorrowRepository borrowRepository;

    @Autowired
    BookCopyRepository bookCopyRepository;

    @Autowired
    BookTypeRepository bookTypeRepository;

    /**
     * 用户注册时，给用户的邮箱发送验证码的业务
     *
     * @param to 用户的邮箱
     * @return 发送到用户邮箱的验证码
     * @author haojie
     */
    public String sendRegisterCaptcha(String to) throws MessagingException {
        String captcha = UUID.randomUUID().toString();
        String text = String.format("你的验证码是%s", captcha);
        emailUtils.sendEmail(to, "图书馆注册验证码", text);
        return captcha;

    }

    /**
     * @author yiwen
     * @param type 发送邮件的种类
     * @return 成功返回msg，失败抛出异常
     * @throws MessagingException
     */
    public HashMap<String,String> sendNotify(String type) throws MessagingException {
        HashMap<String, String> map = new HashMap<>();
        switch (type){
            case "reserve":
                Date currentDate = new Date();
                List<Reservation> allReservation = reservationRepository.findAll();
                //没有超期则剔除
                allReservation.removeIf(r -> currentDate.getTime() <= r.getDeadline().getTime());
                List<String> emailList1 = new ArrayList<>();
                List<String> reserveList = new ArrayList<>();
                //循环加入每个用户对应的预约超期的书
                for(Reservation r:allReservation){
                    String isbn = bookCopyRepository.findById(r.getBookCopyID()).get().getIsbn();
                    String bookName = bookTypeRepository.getBookTypeByISBN(isbn).get().getName();

                    Optional<User> user = userRepository.findById(r.getUserID());
                    String email = user.get().getEmail();

                    if(!emailList1.contains(email)){
                        emailList1.add(email);
                        reserveList.add(bookName);
                    }else {
                        int index = emailList1.indexOf(email);
                        String oldText = reserveList.get(index);
                        String newText = oldText+", "+bookName;
                        reserveList.set(index,newText);
                    }

                    reservationRepository.deleteReservationByBookCopyID(r.getBookCopyID());
                    //将预约超期记录删除
                }

                for(int i = 0; i < emailList1.size(); i ++){
                    String finalText = "同学你好，你预约的书本" + reserveList.get(i) + "已超期，系统已自动删除预约记录。";
                    emailUtils.sendEmail(emailList1.get(i),"图书馆预约超期提醒",finalText);
                }
                map.put("message", "提醒成功！");
                break;
            case "borrow":
                Date currentDate2 = new Date();
                List<Borrow> allBorrow = borrowRepository.findAll();
                //没有超期则剔除
                allBorrow.removeIf(r -> currentDate2.getTime() <= r.getDeadline().getTime());
                List<String> emailList2 = new ArrayList<>();
                List<String> borrowList = new ArrayList<>();
                //循环加入每个用户对应的借阅超期的书
                for(Borrow b:allBorrow){
                    String isbn = bookCopyRepository.getBookCopyByUniqueBookMark(b.getUniqueBookMark()).get().getIsbn();
                    String bookName = bookTypeRepository.getBookTypeByISBN(isbn).get().getName();

                    Optional<User> user = userRepository.findById(b.getUserID());
                    String email = user.get().getEmail();

                    if(!emailList2.contains(email)){
                        emailList2.add(email);
                        borrowList.add(bookName);
                    }else {
                        int index = emailList2.indexOf(email);
                        String oldText = borrowList.get(index);
                        String newText = oldText+", "+bookName;
                        borrowList.set(index,newText);
                    }
                }
                for(int i = 0; i < emailList2.size(); i ++){
                    String finalText = "同学你好，你借阅的书本" + borrowList.get(i) + "已超期，请尽快归还。";
                    emailUtils.sendEmail(emailList2.get(i),"图书馆借阅超期提醒",finalText);
                }
                map.put("message", "提醒成功！");
                break;
            case "fine":
                List<Fine> allFines = fineRepository.findAll();
                List<String> emailList = new ArrayList<>();
                List<String> reasonList = new ArrayList<>();
                //foreach 循环将所有罚款记录取出并且按照每个 email对应的reason添加进list
                for(Fine f:allFines){
                    String reason = f.getReason();
                    long money = f.getMoney();
                    long yuan = money / 100;
                    long jiao = (money % 100) / 10;
                    long fen = (money % 100) % 10;
                    String text = String.format("因%s的%d.%d%d元",reason,yuan,jiao,fen);
                    Optional<User> user = userRepository.findById(f.getUserID());
                    String email = user.get().getEmail();
                    if(!emailList.contains(email)){
                        emailList.add(email);
                        reasonList.add(text);
                    }else {
                        int index = emailList.indexOf(email);
                        String oldReason = reasonList.get(index);
                        String newReason = oldReason+", "+text;
                        reasonList.set(index,newReason);
                    }
//                    if(user.isEmpty()){
//                        throw new UserNotFoundException("用户不存在!");
//                    }
                }

                for(int i = 0;i < emailList.size();i ++){
                    String finalText = "同学你好，你" + reasonList.get(i) + "尚未支付，请尽快缴纳。";
                    emailUtils.sendEmail(emailList.get(i),"图书馆罚款未缴纳提醒",finalText);
                }
                map.put("message", "提醒成功！");
                break;
            default:
                throw new NotifyException("提醒类别错误");
        }
        return map;
    }


}
