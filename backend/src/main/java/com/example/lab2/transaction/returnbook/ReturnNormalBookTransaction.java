package com.example.lab2.transaction.returnbook;

import com.example.lab2.entity.*;
import com.example.lab2.exception.notfound.BookTypeNotFoundException;
import com.example.lab2.exception.notfound.UserNotFoundException;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

/**
 * 还书，如果此时书本状态为正常的业务
 */
@NoArgsConstructor
public class ReturnNormalBookTransaction extends ReturnBookTransaction {


    /**
     * 及时正常还书
     *
     * @param bookCopy
     * @param borrow
     * @param adminID
     * @param adminLibraryID
     * @param currentDate
     * @return
     * @author zhj
     */
    @Override
    public String inTime(BookCopy bookCopy, Borrow borrow, Long adminID, Long adminLibraryID, Date currentDate) {

        bookCopy.setStatus(BookCopy.AVAILABLE);
        bookCopy.setLastReturnDate(currentDate);
        bookCopy.setAdminID(adminID);
        bookCopy.setLibraryID(adminLibraryID);

        //删除借阅条目
        borrowRepository.delete(borrow);
        //更新副本状态
        bookCopyRepository.save(bookCopy);

        //得到管理员
        Optional<User> adminOptional = userRepository.findById(adminID);
        if (adminOptional.isEmpty()) {
            throw new UserNotFoundException("找不到管理员");
        }

        //创建还书记录对象
        ReturnRecord returnRecord = new ReturnRecord(borrow.getUserID(), currentDate, bookCopy.getUniqueBookMark(), adminOptional.get().getUsername(), adminLibraryID,ReturnRecord.OK);

        returnRecordRepository.save(returnRecord);

        return String.format("还书%s成功", bookCopy.getUniqueBookMark());
    }

    /**
     * 书本状态正常，但是此时已经超期了
     *
     * @param bookCopy
     * @param borrow
     * @param adminID
     * @param adminLibraryID
     * @param currentDate
     * @return
     */
    @Override
    public String overTime(BookCopy bookCopy, Borrow borrow, Long adminID, Long adminLibraryID, Date currentDate) {

        //设置bookCopy和borrow对象
        bookCopy.setStatus(BookCopy.AVAILABLE);
        bookCopy.setLastReturnDate(currentDate);
        bookCopy.setAdminID(adminID);
        bookCopy.setLibraryID(adminLibraryID);

        //删除借阅条目
        borrowRepository.delete(borrow);
        //更新副本状态
        bookCopyRepository.save(bookCopy);

        Optional<BookType> bookTypeOptional = bookTypeRepository.getBookTypeByISBN(bookCopy.getIsbn());
        if (bookTypeOptional.isEmpty()) {
            throw new BookTypeNotFoundException("找不到booktype");
        }

        Optional<User> userOptional = userRepository.findById(borrow.getUserID());
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("找不到user");
        }

        //书本超期，要生成罚款
        long fineAmount = bookTypeOptional.get().getPrice() / 4;

        //生成罚款记录
        this.generateFineAndFineRecord(fineAmount,
                userOptional.orElse(null),
                bookTypeOptional.orElse(null),
                bookCopy,
                currentDate);

        //生成还书记录
        this.generateReturnBookRecord(adminID,
                adminLibraryID,
                userOptional.orElse(null),
                bookCopy,
                currentDate);

        //降低用户的信用
        this.userCreditListener.decreaseUserCredit(
                userOptional.orElse(null).getUsername(),
                String.format("借阅图书%s%s超期，信用降低%d分", bookTypeOptional.orElse(null).getName(), bookCopy.getUniqueBookMark(), CREDIT_LOSS_OVERTIME),
                CREDIT_LOSS_OVERTIME
        );

        return String.format("还书%s%s成功，由于借阅超期，%s被罚款%.2f元。此外，他的信用还降低了%d",
                bookTypeOptional.get().getName(), bookCopy.getUniqueBookMark(), userOptional.get().getUsername(), fineAmount / 100.00, CREDIT_LOSS_OVERTIME);

    }

    @Override
    public void generateReturnBookRecord(long adminID, long adminLibraryID, User user, BookCopy bookCopy, Date currentDate) {
        //得到管理员
        Optional<User> adminOptional = userRepository.findById(adminID);
        if (adminOptional.isEmpty()) {
            throw new UserNotFoundException("找不到管理员");
        }
        //创建还书记录对象
        ReturnRecord returnRecord = new ReturnRecord(user.getUser_id(), currentDate, bookCopy.getUniqueBookMark(), adminOptional.get().getUsername(), adminLibraryID,ReturnRecord.OK);

        returnRecordRepository.save(returnRecord);
    }

}
