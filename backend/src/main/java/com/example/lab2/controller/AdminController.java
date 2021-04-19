package com.example.lab2.controller;


import com.example.lab2.dao.BookTypeRepository;
import com.example.lab2.dao.LibraryRepository;
import com.example.lab2.exception.UploadException;
import com.example.lab2.request.upload.AddBookCopyRequest;
import com.example.lab2.response.GeneralResponse;
import com.example.lab2.request.upload.UploadNewBookRequest;
import com.example.lab2.service.UploadService;
import com.example.lab2.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Objects;

@RestController
@RequestMapping("/admin")
@CrossOrigin(allowCredentials = "true", originPatterns = "*")
public class AdminController {

    @Resource(name = "uploadService")
    UploadService uploadService;

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
        addBookCopyRequest.setLibraryID(libraryID+"");

        GeneralResponse generalResponse = uploadService.addBookCopy(addBookCopyRequest);
        return ResponseEntity.ok(generalResponse);


    }


}
