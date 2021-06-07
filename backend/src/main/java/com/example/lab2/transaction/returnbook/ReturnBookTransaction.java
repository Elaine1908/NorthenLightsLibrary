package com.example.lab2.transaction.returnbook;

import com.example.lab2.dao.*;
import com.example.lab2.dao.record.FineRecordRepository;
import com.example.lab2.dao.record.ReturnRecordRepository;
import com.example.lab2.entity.*;
import com.example.lab2.exception.borrow.NotBorrowedException;
import com.example.lab2.exception.notfound.BookCopyNotFoundException;
import com.example.lab2.exception.notfound.LibraryNotFoundException;
import com.example.lab2.exception.notfound.UserNotFoundException;
import com.example.lab2.listener.UserCreditListener;
import com.example.lab2.request.borrow.ReturnSingleBookRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.plaf.PanelUI;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Component
public abstract class ReturnBookTransaction {

    @Autowired
    protected BookCopyRepository bookCopyRepository;

    @Autowired
    protected BorrowRepository borrowRepository;

    @Autowired
    protected LibraryRepository libraryRepository;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected BookTypeRepository bookTypeRepository;

    @Autowired
    protected FineRepository fineRepository;

    @Autowired
    protected FineRecordRepository fineRecordRepository;

    @Autowired
    protected ReturnRecordRepository returnRecordRepository;

    @Autowired
    UserCreditListener userCreditListener;

    //逾期还书信用降低20，书本损坏信用降低30，书本丢失信用降低40
    public final static int CREDIT_LOSS_OVERTIME = 20;
    public final static int CREDIT_LOSS_DAMAGE = 30;
    public final static int CREDIT_LOSS_LOST = 40;


    public abstract String inTime(BookCopy bookCopy, Borrow borrow, Long adminID, Long adminLibraryID, Date currentDate);


    public abstract String overTime(BookCopy bookCopy, Borrow borrow, Long adminID, Long adminLibraryID, Date currentDate);


    public String doReturn(ReturnSingleBookRequest returnSingleBookRequest, String admin, Long adminLibraryID) {
        String uniqueBookMark = returnSingleBookRequest.getUniqueBookMark();

        //看看副本存不存在
        Optional<BookCopy> bookCopyOptional = bookCopyRepository.getBookCopyByUniqueBookMark(
                uniqueBookMark);

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

        //得到借阅的截止时间
        Date deadline = borrowOptional.get().getDeadline();
        Date currentDate = new Date();

        //没有超期的情况
        if (currentDate.getTime() <= deadline.getTime()) {
            return inTime(bookCopyOptional.get(), borrowOptional.get(), adminID, adminLibraryID, currentDate);
        } else {//超期的情况
            return overTime(bookCopyOptional.get(), borrowOptional.get(), adminID, adminLibraryID, currentDate);
        }

    }


    protected void generateFineAndFineRecord(long fineAmount, User user, BookType bookType, BookCopy bookCopy, Date currentDate) {
        String reason = String.format("借阅%s%s超期罚款",
                bookType.getName(), bookCopy.getUniqueBookMark());

        String randomUUID = UUID.randomUUID().toString();

        //创建罚款对象
        Fine fine = new Fine(fineAmount, user.getUser_id(), reason, currentDate, randomUUID);
        fineRepository.save(fine);

        //创建罚款记录对象
        FineRecord fineRecord = new FineRecord(user.getUser_id(), currentDate, fineAmount, FineRecord.UNPAID, reason, randomUUID);
        fineRecordRepository.save(fineRecord);
    }


    protected void generateReturnBookRecord(long adminID, long adminLibraryID, User user, BookCopy bookCopy, Date currentDate) {
        //得到管理员
        Optional<User> adminOptional = userRepository.findById(adminID);
        if (adminOptional.isEmpty()) {
            throw new UserNotFoundException("找不到管理员");
        }
        //创建还书记录对象
        ReturnRecord returnRecord = new ReturnRecord(user.getUser_id(), currentDate, bookCopy.getUniqueBookMark(), adminOptional.get().getUsername(), adminLibraryID);

        returnRecordRepository.save(returnRecord);
    }

}
