package com.example.lab2.controller;


import com.example.lab2.dao.BookTypeRepository;
import com.example.lab2.dao.LibraryRepository;
import com.example.lab2.exception.UploadException;
import com.example.lab2.request.borrow.BorrowBookRequest;
import com.example.lab2.request.borrow.BorrowReservedBookRequest;
import com.example.lab2.request.upload.AddBookCopyRequest;
import com.example.lab2.response.GeneralResponse;
import com.example.lab2.request.upload.UploadNewBookRequest;
import com.example.lab2.service.BorrowService;
import com.example.lab2.service.NormalUserService;
import com.example.lab2.service.UploadService;
import com.example.lab2.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.PushBuilder;
import javax.validation.Valid;
import java.util.Objects;

@RestController
@RequestMapping("/admin")
@CrossOrigin(allowCredentials = "true", originPatterns = "*")
public class AdminController {

    @Resource(name = "uploadService")
    UploadService uploadService;

    @Resource(name = "borrowService")
    BorrowService borrowService;

    @Resource(name = "normalUserService")
    NormalUserService normalUserService;

    @Autowired
    LibraryRepository libraryRepository;

    @Autowired
    BookTypeRepository bookTypeRepository;

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

        //从jwt中读取出管理员的上班地点，并缺省设置添加副本的library为管理员上班的library
        String token = request.getHeader("token");
        Long libraryID = JwtUtils.getLibraryID(token);
        addBookCopyRequest.setLibraryID(libraryID + "");

        GeneralResponse generalResponse = uploadService.addBookCopy(addBookCopyRequest);
        return ResponseEntity.ok(generalResponse);


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

        //进入业务层
        GeneralResponse generalResponse = borrowService.lendBookToUser(
                borrowBookRequest.getUniqueBookMark(),
                borrowBookRequest.getUsername(),
                adminLibraryID
        );

        //把结果返回给前端
        return ResponseEntity.ok(generalResponse);

    }

    /**
     * 管理员把用户预约的书借出
     * @param borrowReservedBookRequest
     * @param bindingResult
     * @param httpServletRequest
     * @return
     */
    @PostMapping("/lendReservedBookToUser")
    public ResponseEntity<?> lendReservedBookToUser(@Valid @RequestBody BorrowReservedBookRequest borrowReservedBookRequest,
                                                    BindingResult bindingResult,
                                                    HttpServletRequest httpServletRequest) {

        if (bindingResult.hasFieldErrors()) {
            throw new IllegalArgumentException(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        String token = httpServletRequest.getHeader("token");

        //获得管理员现在在哪个图书馆上班
        Long adminLibraryID = JwtUtils.getLibraryID(token);

        GeneralResponse generalResponse = borrowService.lendReservedBookToUser(
                borrowReservedBookRequest.getUsername(),
                borrowReservedBookRequest.getUniqueBookMarkList(),
                adminLibraryID
        );

        //把结果返回给前端
        return ResponseEntity.ok(generalResponse);

    }

}
