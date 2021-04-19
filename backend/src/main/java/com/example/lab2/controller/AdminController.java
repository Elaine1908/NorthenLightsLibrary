package com.example.lab2.controller;


import com.example.lab2.dao.BookTypeRepository;
import com.example.lab2.dao.LibraryRepository;
import com.example.lab2.exception.UploadException;
import com.example.lab2.response.GeneralResponse;
import com.example.lab2.request.upload.UploadNewBookRequest;
import com.example.lab2.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

@RestController
@RequestMapping("/admin")
@CrossOrigin(allowCredentials = "true",originPatterns = "*")
public class AdminController {


    @Autowired
    LibraryRepository libraryRepository;

    @Autowired
    BookTypeRepository bookTypeRepository;

    /**
     * 管理员上传一种新书。要求isbn必须是唯一的
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

        GeneralResponse response = UploadService.handleUpload(uploadNewBookRequest);
        return ResponseEntity.ok(response);
    }


}
