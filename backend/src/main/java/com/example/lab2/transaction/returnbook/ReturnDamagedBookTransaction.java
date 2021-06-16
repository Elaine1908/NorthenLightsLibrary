package com.example.lab2.transaction.returnbook;

import com.example.lab2.entity.*;
import com.example.lab2.exception.notfound.BookTypeNotFoundException;
import com.example.lab2.exception.notfound.UserNotFoundException;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Optional;

/**
 * 还书，还书时书本损坏的业务
 */
@NoArgsConstructor
public class ReturnDamagedBookTransaction extends ReturnBookTransaction {

    /**
     * 损坏书本的按期归还
     *
     * @param bookCopy
     * @param borrow
     * @param adminID
     * @param adminLibraryID
     * @param currentDate
     * @return
     */
    @Override
    public String inTime(BookCopy bookCopy, Borrow borrow, Long adminID, Long adminLibraryID, Date currentDate) {
        //设置bookCopy和borrow对象
        bookCopy.setStatus(BookCopy.DAMAGED);
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

        //书本超期，要生成罚款.这里是损坏，则罚款原价的二分之一
        //书本超期，要生成罚款
        long fineAmount = bookTypeOptional.orElse(null).getPrice() / 2;

        //生成罚款记录
        this.generateFineAndFineRecord(fineAmount,
                userOptional.orElse(null),
                bookTypeOptional.orElse(null),
                bookCopy,
                currentDate,
                String.format("损坏图书%s%s罚款", bookTypeOptional.orElse(null).getName(), bookCopy.getUniqueBookMark())
        );

        //生成还书记录
        this.generateReturnBookRecord(adminID,
                adminLibraryID,
                userOptional.orElse(null),
                bookCopy,
                currentDate);

        //降低用户的信用30分
        this.userCreditListener.decreaseUserCredit(
                userOptional.orElse(null).getUsername(),
                String.format("损坏图书%s%s，信用降低%d分", bookTypeOptional.orElse(null).getName(), bookCopy.getUniqueBookMark(), CREDIT_LOSS_DAMAGE),
                CREDIT_LOSS_DAMAGE
        );

        return String.format("还书%s%s成功，由于书本损坏，%s被罚款%.2f元。此外，他的信用还降低了%d分",
                bookTypeOptional.orElse(null).getName(), bookCopy.getUniqueBookMark(), userOptional.orElse(null).getUsername(), fineAmount / 100.00, CREDIT_LOSS_DAMAGE);
    }

    /**
     * 损坏书本的超期归还。其实这里和按期归还是一样的，都是罚款原价的一半
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
    public String overTime(BookCopy bookCopy, Borrow borrow, Long adminID, Long adminLibraryID, Date currentDate) {
        return this.inTime(bookCopy, borrow, adminID, adminLibraryID, currentDate);
    }

    @Override
    public void generateReturnBookRecord(long adminID, long adminLibraryID, User user, BookCopy bookCopy, Date currentDate) {
        //得到管理员
        Optional<User> adminOptional = userRepository.findById(adminID);
        if (adminOptional.isEmpty()) {
            throw new UserNotFoundException("找不到管理员");
        }
        //创建还书记录对象
        ReturnRecord returnRecord = new ReturnRecord(user.getUser_id(), currentDate, bookCopy.getUniqueBookMark(), adminOptional.orElse(null).getUsername(), adminLibraryID, ReturnRecord.DAMAGED);

        returnRecordRepository.save(returnRecord);
    }

}
