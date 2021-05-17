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
        long fineAmount = bookTypeOptional.get().getPrice() / 2;
        String reason = String.format("借阅%s%s损坏罚款",
                bookTypeOptional.get().getName(), bookCopy.getUniqueBookMark());

        //创建罚款对象
        Fine fine = new Fine(fineAmount, userOptional.get().getUser_id(), reason, currentDate);
        fineRepository.save(fine);

        return String.format("还书%s%s成功，由于书本损坏，%s被罚款%.2f元",
                bookTypeOptional.get().getName(), bookCopy.getUniqueBookMark(), userOptional.get().getUsername(), fineAmount / 100.00);
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
        return inTime(bookCopy, borrow, adminID, adminLibraryID, currentDate);
    }

}
