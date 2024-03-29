package com.example.lab2.controller;


import com.example.lab2.dao.BookTypeRepository;
import com.example.lab2.dao.LibraryRepository;
import com.example.lab2.dto.bookcopy.ShowBookCopyDTO;
import com.example.lab2.dto.record.*;
import com.example.lab2.exception.UploadException;
import com.example.lab2.request.borrow.BorrowBookRequest;
import com.example.lab2.request.borrow.BorrowReservedBookRequest;
import com.example.lab2.request.borrow.ReturnBookRequest;
import com.example.lab2.request.comment.DeleteCommentRequest;
import com.example.lab2.request.comment.DeleteReplyRequest;
import com.example.lab2.request.sensitive.AddToSensitiveRequest;
import com.example.lab2.request.sensitive.RemoveFromSensitiveRequest;
import com.example.lab2.request.upload.AddBookCopyRequest;
import com.example.lab2.response.GeneralResponse;
import com.example.lab2.request.upload.UploadNewBookRequest;
import com.example.lab2.service.*;
import com.example.lab2.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Resource(name = "uploadService")
    UploadService uploadService;

    @Resource(name = "borrowService")
    BorrowService borrowService;

    @Resource(name = "searchService")
    SearchService searchService;

    @Resource(name = "normalUserService")
    NormalUserService normalUserService;

    @Resource(name = "commentService")
    CommentService commentService;

    @Autowired
    LibraryRepository libraryRepository;

    @Autowired
    BookTypeRepository bookTypeRepository;

    @Resource(name = "adminService")
    AdminService adminService;

    /**
     * 管理员上传一种新书。要求isbn必须是唯一的
     *
     * @param uploadNewBookRequest
     * @param bindingResult
     * @return
     */
    @PostMapping(value = "/uploadNewBook")
    public ResponseEntity<GeneralResponse> handleUpload(
            @ModelAttribute @Valid UploadNewBookRequest uploadNewBookRequest, BindingResult bindingResult) {

        if (bindingResult.hasFieldErrors()) {
            throw new UploadException(
                    Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage()
            );
        }

        GeneralResponse response = uploadService.handleUpload(uploadNewBookRequest);
        return ResponseEntity.ok(response);
    }


    /**
     * 管理员添加一本书的副本的接口
     *
     * @param addBookCopyRequest
     * @param bindingResult
     * @return
     */
    @PostMapping("/addBookCopy")
    public ResponseEntity<GeneralResponse> addBookCopy(@Valid @RequestBody AddBookCopyRequest addBookCopyRequest,

                                                       BindingResult bindingResult, HttpServletRequest request) {
        //检查是否存在输入参数错误
        if (bindingResult.hasFieldErrors()) {
            throw new UploadException(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage())
                    ;
        }

        GeneralResponse generalResponse = uploadService.addBookCopy(addBookCopyRequest);
        return ResponseEntity.ok(generalResponse);
    }


    /**
     * 管理员输入副本号后前端页面显示副本信息的接口
     *
     * @param isbn
     * @return
     */
    @GetMapping("/showBookToUser")
    public ResponseEntity<HashMap<String, Object>> showBookToUser(@RequestParam("isbn") String isbn) {
        HashMap<String, Object> result = new HashMap<>();
        ShowBookCopyDTO bookCopy = searchService.getBookCopyByIsbn(isbn);
        result.put("book", bookCopy);
        return ResponseEntity.ok(result);
    }

    /**
     * 管理员把书借给用户的接口
     *
     * @param borrowBookRequest
     * @param bindingResult
     * @param httpServletRequest
     * @return
     */
    @PostMapping("/lendBookToUser")
    public ResponseEntity<GeneralResponse> lendBookToUser(
            @Valid @RequestBody BorrowBookRequest borrowBookRequest,
            BindingResult bindingResult,
            HttpServletRequest httpServletRequest) {

        //如果输入的参数不完整，就抛出异常！
        if (bindingResult.hasFieldErrors()) {
            throw new IllegalArgumentException(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }

        String token = httpServletRequest.getHeader("token");

        //获得管理员现在在哪个图书馆上班？
        Long adminLibraryID = JwtUtils.getLibraryID(token);
        //得到admin的账号
        String admin = JwtUtils.getUserName(token);

        //进入业务层
        GeneralResponse generalResponse = borrowService.lendBookToUser(
                borrowBookRequest.getUniqueBookMarkList(),
                borrowBookRequest.getUsername(),
                adminLibraryID,
                admin
        );

        //把结果返回给前端
        return ResponseEntity.ok(generalResponse);

    }

    /**
     * 管理员把用户预约的书借出
     *
     * @param borrowReservedBookRequest
     * @param bindingResult
     * @param httpServletRequest
     * @return
     */
    @PostMapping("/lendReservedBookToUser")
    public ResponseEntity<GeneralResponse> lendReservedBookToUser(@Valid @RequestBody BorrowReservedBookRequest borrowReservedBookRequest,
                                                                  BindingResult bindingResult,
                                                                  HttpServletRequest httpServletRequest) {

        if (bindingResult.hasFieldErrors()) {
            throw new IllegalArgumentException(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        String token = httpServletRequest.getHeader("token");

        //获得管理员现在在哪个图书馆上班
        Long adminLibraryID = JwtUtils.getLibraryID(token);
        //得到admin的账号
        String admin = JwtUtils.getUserName(token);

        GeneralResponse generalResponse = borrowService.lendReservedBookToUser(
                borrowReservedBookRequest.getUsername(),
                borrowReservedBookRequest.getUniqueBookMarkList(),
                adminLibraryID, admin
        );

        //把结果返回给前端
        return ResponseEntity.ok(generalResponse);

    }

    @PostMapping("/receiveBookFromUser")
    public ResponseEntity<List<String>> receiveBookFromUser(@Valid @RequestBody ReturnBookRequest returnBookRequest, BindingResult bindingResult,
                                                            HttpServletRequest httpServletRequest) {

        if (bindingResult.hasFieldErrors()) {
            throw new IllegalArgumentException(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        String token = httpServletRequest.getHeader("token");

        //获得管理员现在在哪个图书馆上班
        Long adminLibraryID = JwtUtils.getLibraryID(token);
        //得到admin的账号
        String admin = JwtUtils.getUserName(token);

        List<String> resList = normalUserService.returnBooks(returnBookRequest.getUniqueBookMarkList(), adminLibraryID, admin);

        //把结果返回给前端
        return ResponseEntity.ok(resList);
    }

    @GetMapping("/record")
    public ResponseEntity<HashMap<String, Object>> searchRecordByUsername(@RequestParam("username") String username) {
        List<ReserveRecordDTO> reserveRecordDTOS = normalUserService.getReserveRecord(username);
        List<BorrowRecordDTO> borrowRecordDTOS = normalUserService.getBorrowRecord(username);
        List<ReturnRecordDTO> returnRecordDTOS = normalUserService.getReturnRecord(username);
        List<FineRecordDTO> fineRecordDTOS = normalUserService.getFineRecord(username);

        //加入result
        HashMap<String, Object> result = new HashMap<>();
        result.put("reserveRecordList", reserveRecordDTOS);
        result.put("borrowRecordList", borrowRecordDTOS);
        result.put("returnRecordList", returnRecordDTOS);
        result.put("fineRecordList", fineRecordDTOS);
        return ResponseEntity.ok(result);

    }

    @GetMapping("/recordOfBook")
    public ResponseEntity<HashMap<String, Object>> getRecordByUniqueBookMark(@Param("isbn") String isbn) {

        List<RecordAboutBookCopyDTO> recordAboutBookCopyDTOS = normalUserService.getBookCopyRecord(isbn);
        //加入result
        HashMap<String, Object> result = new HashMap<>();
        result.put("recordList", recordAboutBookCopyDTOS);
        return ResponseEntity.ok(result);
    }


    @GetMapping("/sensitiveWordList")
    public ResponseEntity<HashMap<String, Object>> getSensitiveWordList() {
        //去业务层找到敏感词列表
        List<String> sensitiveWordList = adminService.findAllSensitiveWord();

        //生成传输给前端的对象
        HashMap<String, Object> res = new HashMap<>();
        res.put("sensitiveWordList", sensitiveWordList);

        //返回给前端
        return ResponseEntity.ok(res);
    }


    /**
     * 前端向敏感词检查器中添加敏感词的接口
     *
     * @param addToSensitiveRequest
     * @return
     */
    @PostMapping("/addToSensitive")
    public ResponseEntity<GeneralResponse> addToSensitive(@RequestBody AddToSensitiveRequest addToSensitiveRequest) {

        List<String> addToSensitiveList = addToSensitiveRequest.getAddToSensitiveList();

        String msg = adminService.addToSensitive(addToSensitiveList);

        return ResponseEntity.ok(new GeneralResponse(msg));

    }

    /**
     * 前端从敏感词检查器中删除敏感词的接口
     *
     * @param removeFromSensitiveRequest
     * @return
     */
    @PostMapping("/removeFromSensitive")
    public ResponseEntity<GeneralResponse> removeFromSensitive(
            @RequestBody RemoveFromSensitiveRequest removeFromSensitiveRequest) {

        List<String> removeFromSensitiveList = removeFromSensitiveRequest.getRemoveFromSensitiveList();

        String msg = adminService.removeFromSensitive(removeFromSensitiveList);

        return ResponseEntity.ok(new GeneralResponse(msg));
    }

    @PostMapping("/deleteComment")
    public ResponseEntity<?> deleteComment(@RequestBody DeleteCommentRequest deleteCommentRequest, HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("token");
        String username = JwtUtils.getUserName(token);
        GeneralResponse generalResponse = commentService.deleteComment(deleteCommentRequest.getCommentID(), username);
        return ResponseEntity.ok(generalResponse);

    }

    @PostMapping("/deleteReply")
    public ResponseEntity<?> deleteReply(@RequestBody DeleteReplyRequest deleteReplyRequest, HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("token");
        String username = JwtUtils.getUserName(token);
        GeneralResponse generalResponse = commentService.deleteReply(deleteReplyRequest.getCommentID(), username);
        return ResponseEntity.ok(generalResponse);

    }
}
