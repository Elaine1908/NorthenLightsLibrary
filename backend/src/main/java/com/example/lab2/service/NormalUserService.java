package com.example.lab2.service;

import com.example.lab2.dao.*;
import com.example.lab2.dao.record.*;
import com.example.lab2.dto.bookcopy.BorrowedBookCopyDTO;
import com.example.lab2.dto.bookcopy.ReservedBookCopyDTO;
import com.example.lab2.dto.record.*;
import com.example.lab2.entity.*;
import com.example.lab2.exception.comment.*;
import com.example.lab2.exception.notfound.BookCopyNotFoundException;
import com.example.lab2.exception.notfound.BookTypeNotFoundException;
import com.example.lab2.exception.notfound.CommentNotFoundException;
import com.example.lab2.exception.notfound.UserNotFoundException;
import com.example.lab2.request.borrow.ReturnSingleBookRequest;
import com.example.lab2.response.GeneralResponse;
import com.example.lab2.response.UserInfoResponse;
import com.example.lab2.transaction.returnbook.ReturnBookTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

@Service("normalUserService")
public class NormalUserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookCopyRepository bookCopyRepository;
    @Autowired
    BookTypeRepository bookTypeRepository;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    ReplyRepository replyRepository;
    @Autowired
    LibraryRepository libraryRepository;
    @Autowired
    BorrowRepository borrowRepository;
    @Autowired
    ReserveRecordRepository reserveRecordRepository;
    @Autowired
    BorrowRecordRepository borrowRecordRepository;
    @Autowired
    ReturnRecordRepository returnRecordRepository;

    @Autowired
    FineRecordRepository fineRecordRepository;

    @Autowired
    CreditRecordRepository creditRecordRepository;

    @Autowired
    private AutowireCapableBeanFactory autowireCapableBeanFactory;

    public UserInfoResponse userInfo(String username) {
        User user = userRepository.getUserByUsername(username);
        if (user == null) {
            throw new UserNotFoundException("用户不存在");
        }
        int credit = user.getCredit();
        String role = user.getRole();

        //先根据用户信息创建response对象
        UserInfoResponse userInfoResponse = new UserInfoResponse(username, credit, role);

        //获得用户借阅的所有副本信息
        List<BorrowedBookCopyDTO> borrowedBooks = bookCopyRepository.getBorrowedBookCopiesByUsername(username);

        userInfoResponse.setBorrowedBooks(borrowedBooks);

        //获得用户预约的所有副本信息
        List<ReservedBookCopyDTO> reservedBooks = bookCopyRepository.getAllReservedBooksByUsername(username);

        userInfoResponse.setReservedBooks(reservedBooks);

        return userInfoResponse;


    }

    public List<String> returnBooks(List<ReturnSingleBookRequest> books, Long adminLibraryID, String admin) {

        List<String> stringList = new ArrayList<>();

        //还书成功的数目
        int successfulCount = 0;

        for (ReturnSingleBookRequest returnSingleBookRequest : books) {

            try {
                //尝试还书
                String res = this.returnOnlyOneBook(returnSingleBookRequest, adminLibraryID, admin);

                //如果没有抛出异常，说明成功
                stringList.add(res);

                successfulCount += 1;

            } catch (RuntimeException e) {

                //还书失败，提示用户
                stringList.add(e.getMessage());

            }

        }

        stringList.add(String.format(
                "共成功还掉了%d本图书", successfulCount
        ));

        //返回结果
        return stringList;
    }

    /**
     * 只还一本书的接口。
     *
     * @param returnSingleBookRequest
     * @param adminLibraryID
     * @param admin
     * @return
     * @author zhj
     */
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public String returnOnlyOneBook(
            ReturnSingleBookRequest returnSingleBookRequest,
            Long adminLibraryID, String admin) {

        //初始化状态到类名的哈希表
        HashMap<String, String> statusToClassName = new HashMap<>();
        statusToClassName.put("ok", "com.example.lab2.transaction.returnbook.ReturnNormalBookTransaction");
        statusToClassName.put("damaged", "com.example.lab2.transaction.returnbook.ReturnDamagedBookTransaction");
        statusToClassName.put("lost", "com.example.lab2.transaction.returnbook.ReturnLostBookTransaction");

        try {
            //反射创建对应的transaction
            Class<?> clz = Class.forName(statusToClassName.get(returnSingleBookRequest.getStatus()));
            ReturnBookTransaction returnBookTransaction = (ReturnBookTransaction) clz.getConstructor().newInstance();

            //给反射创建的transaction注入bean
            autowireCapableBeanFactory.autowireBean(returnBookTransaction);

            //尝试还书
            String res = returnBookTransaction.doReturn(returnSingleBookRequest, admin, adminLibraryID);
            return res;
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e.getMessage());
        }


    }


    /**
     * 获取所有的预约记录
     *
     * @param username
     * @return
     * @author zyw
     */
    public List<ReserveRecordDTO> getReserveRecord(String username) {
        //看看用户存不存在
        Optional<User> userOptional = userRepository.findByName(username);
        if (!userOptional.isPresent()) {
            throw new UserNotFoundException("找不到这个用户！");
        }

        return reserveRecordRepository.getReserveRecordByUsername(username);
    }

    /**
     * 获取所有的借阅记录
     *
     * @param username
     * @return
     * @author zyw
     */
    public List<BorrowRecordDTO> getBorrowRecord(String username) {
        //看看用户存不存在
        Optional<User> userOptional = userRepository.findByName(username);
        if (!userOptional.isPresent()) {
            throw new UserNotFoundException("找不到这个用户！");
        }
        return borrowRecordRepository.getBorrowRecordByUsername(username);
    }

    /**
     * 获取所有的还书记录
     *
     * @param username
     * @return
     * @author zyw
     */
    public List<ReturnRecordDTO> getReturnRecord(String username) {
        //看看用户存不存在
        Optional<User> userOptional = userRepository.findByName(username);
        if (!userOptional.isPresent()) {
            throw new UserNotFoundException("找不到这个用户！");
        }
        return returnRecordRepository.getReturnRecordByUsername(username);
    }

    /**
     * 获取所有的罚款记录
     *
     * @param username
     * @return
     * @author zyw
     */
    public List<FineRecordDTO> getFineRecord(String username) {
        //看看用户存不存在
        Optional<User> userOptional = userRepository.findByName(username);
        if (!userOptional.isPresent()) {
            throw new UserNotFoundException("找不到这个用户！");
        }
        return fineRecordRepository.getFineRecordByUsername(username);
    }


    public List<RecordAboutBookCopyDTO> getBookCopyRecord(String isbn) {
        Optional<BookCopy> bookCopyOptional = bookCopyRepository.getBookCopyByUniqueBookMark(isbn);
        if (bookCopyOptional.isEmpty()) {
            throw new BookCopyNotFoundException("找不到这个副本！");
        }

        //分别根据uniqueBookMark得到借阅记录，归还记录和预约记录
        List<ReserveRecordDTO> reserveRecordDTOList = reserveRecordRepository.getBookCopyReserveRecordByUniqueBookMark(isbn);
        List<ReturnRecordDTO> returnRecordDTOList = returnRecordRepository.getBookCopyReturnRecordByUniqueBookMark(isbn);
        List<BorrowRecordDTO> borrowRecordDTOList = borrowRecordRepository.getBookCopyBorrowRecordByUniqueBookMark(isbn);

        //最终的结果list
        List<RecordAboutBookCopyDTO> resList = new ArrayList<>();

        //将三个list中的内容加入到结果list中去
        resList.addAll(reserveRecordDTOList);
        resList.addAll(returnRecordDTOList);
        resList.addAll(borrowRecordDTOList);

        //按时间排序
        resList.sort(Comparator.comparing(RecordAboutBookCopyDTO::getTime));

        //返回结果
        return resList;
    }

    /**
     * 根据用户名，获得所有的信用记录的过程
     *
     * @param username 用户名
     * @return 信用记录的列表
     */
    public List<CreditRecordDTO> getCreditRecordListByUsername(String username) {
        List<CreditRecordDTO> res = creditRecordRepository.getCreditRecordByUsername(username);
        return res;
    }

    /**
     * 用户发起评论
     *
     * @param username
     * @param isbn
     * @param content
     * @param rate
     * @author yiwen
     */
    public GeneralResponse postComment(String username, String isbn, String content, long rate) {
        //检查用户存不存在，并得到用户的useid
        Optional<User> userOptional = userRepository.findByName(username);
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("找不到这个用户");
        }

        //检查isbn对应的booktype存不存在
        Optional<BookType> bookTypeOptional = bookTypeRepository.getBookTypeByISBN(isbn);
        if (bookTypeOptional.isEmpty()) {
            throw new BookTypeNotFoundException("找不到这个图书");
        }

        //检查rate大小是否合理
        if (rate > 10 || rate < 0) {
            throw new RateOutOfRangeException("评分只能在0-10之间");
        }

        //检查评论是否存在
        Optional<Comment> commentOptional =
                commentRepository.getCommentsByUserIDAndIsbn(userOptional.orElse(null).getUser_id(), isbn);
        if (commentOptional.isPresent()) {
            throw new CommentAlreadyExistException("你已经评论过此书，不能重复评论");
        }

        //检查读者是否有借阅过这本书且还书状态正常
        List<ReturnRecord> returnRecord = returnRecordRepository.getReturnRecordByUserIDAndISBN(userOptional.orElse(null).getUser_id(), isbn);
        if (checkReturnBook(returnRecord) == 0) {
            throw new NoReturnRecordException("你还没有借阅过此书，无法评论");
        }
        if (checkReturnBook(returnRecord) == 1) {
            throw new ReturnRecordNotOkException("你归还书本时状态非正常，无法评论");
        }

        //运行到这里说明符合评论条件，添加评论
        Date date = new Date();
        Comment comment = new Comment(userOptional.orElse(null).getUser_id(), isbn, content, date, false, false, rate);
        commentRepository.save(comment);
        return new GeneralResponse("评论成功");
    }

    /**
     * 检查用户是否借阅过评论的书本；归还状态是否完好
     *
     * @param list
     * @return 0：没有借阅记录 1：借阅且所有副本归还状态非正常 2：借阅且至少有一本副本归还状态正常
     */
    private int checkReturnBook(List<ReturnRecord> list) {
        if (list.size() == 0) {
            return 0;
        }
        for (ReturnRecord r : list) {
            try {
                if (r.getStatus().equals("ok")) {
                    return 2;
                }
            } catch (Exception ignored) {
            }
        }
        return 1;
    }

    /**
     * 用户发起回复
     *
     * @param username
     * @param commentID
     * @param replyID
     * @param content
     * @author yiwen
     */
    public GeneralResponse postReply(String username, Long commentID, Long replyID, String content) {
        //检查用户存不存在，并得到用户的userID
        Optional<User> userOptional = userRepository.findByName(username);
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("找不到这个用户");
        }

        //如果又有commentID又有replyID，抛出异常
        if (commentID != null && replyID != null) {
            throw new TwoIDAtTheSameTimeException("传输参数异常，replyID和commentID两者应当只有一个");
        }

        //commentID非空的情况：回复评论
        if (commentID != null) {
            //看看评论是否存在
            Optional<Comment> commentOptional = commentRepository.findById(commentID);
            if (commentOptional.isEmpty()) {
                throw new CommentNotFoundException("这条评论不存在");
            } else {
                //获取被回复的用户的id
                long replied_user_id = commentOptional.orElse(null).getUserID();
                Date date = new Date();
                Reply reply = new Reply(userOptional.orElse(null).getUser_id(), commentID, content, date, false, false, replied_user_id);
                replyRepository.save(reply);
            }

        }
        //replyID非空的情况：回复回复
        else {
            //看看回复是否存在
            Optional<Reply> replyOptional = replyRepository.findById(replyID);
            if (replyOptional.isEmpty()) {
                throw new CommentNotFoundException("这条回复不存在");
            } else {
                //获取被回复的用户的id
                long replied_user_id = replyOptional.orElse(null).getUserID();
                Date date = new Date();
                Reply reply = new Reply(userOptional.orElse(null).getUser_id(), replyOptional.orElse(null).getCommentID(), content, date, false, false, replied_user_id);
                replyRepository.save(reply);
            }
        }
        return new GeneralResponse("回复成功");
    }

    /**
     * 用户删除自己的评论
     *
     * @param commentID
     * @author yiwen
     */
    public GeneralResponse deleteComment(Long commentID, String username) {
        //检查用户存不存在，并得到用户的userID
        Optional<User> userOptional = userRepository.findByName(username);
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("找不到这个用户");
        }

        //看看评论是否存在
        Optional<Comment> commentOptional = commentRepository.findById(commentID);
        if (commentOptional.isEmpty()) {
            throw new CommentNotFoundException("这条评论不存在");
        }

        //看看评论的主人是不是这个用户
        if (commentOptional.orElse(null).getUserID() != userOptional.orElse(null).getUser_id()) {
            throw new CommentMismatchException("这个评论的主人不是你");
        } else {
            Comment comment = commentOptional.orElse(null);
            comment.setDeletedBySelf(true);//用户删除评论
            commentRepository.save(comment);

        }
        return new GeneralResponse("成功删除评论");
    }

    /**
     * 用户删除自己的回复
     *
     * @param commentID
     * @author yiwen
     */
    public GeneralResponse deleteReply(Long commentID, String username) {
        //检查用户存不存在，并得到用户的userID
        Optional<User> userOptional = userRepository.findByName(username);
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("找不到这个用户");
        }
        //看看回复是否存在
        Optional<Reply> replyOptional = replyRepository.findById(commentID);
        if (replyOptional.isEmpty()) {
            throw new CommentNotFoundException("这条回复不存在");
        }

        if (replyOptional.orElse(null).getUserID() != userOptional.orElse(null).getUser_id()) {
            throw new CommentMismatchException("这个回复的主人不是你");
        } else {
            Reply reply = replyOptional.orElse(null);
            reply.setDeletedBySelf(true);//用户删除回复
            replyRepository.save(reply);
        }

        return new GeneralResponse("成功删除回复");
    }
}
