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

        //创建还书记录对象
        ReturnRecord returnRecord = new ReturnRecord(borrow.getUserID(), currentDate, bookCopy.getUniqueBookMark(), adminID, adminLibraryID);
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
        String reason = String.format("借阅%s%s超期罚款",
                bookTypeOptional.get().getName(), bookCopy.getUniqueBookMark());

        String randomUUID = UUID.randomUUID().toString();

        //创建罚款对象
        Fine fine = new Fine(fineAmount, userOptional.get().getUser_id(), reason, currentDate, randomUUID);
        fineRepository.save(fine);

        //创建罚款记录对象
        FineRecord fineRecord = new FineRecord(userOptional.get().getUser_id(), currentDate, fineAmount, FineRecord.UNPAID, reason, randomUUID);
        fineRecordRepository.save(fineRecord);

        //创建还书记录对象
        ReturnRecord returnRecord = new ReturnRecord(userOptional.get().getUser_id(), currentDate, bookCopy.getUniqueBookMark(), adminID, adminLibraryID);
        returnRecordRepository.save(returnRecord);

        return String.format("还书%s%s成功，由于借阅超期，%s被罚款%.2f元",
                bookTypeOptional.get().getName(), bookCopy.getUniqueBookMark(), userOptional.get().getUsername(), fineAmount / 100.00);

    }

}
