package com.example.lab2.controller;


import com.example.lab2.request.borrow.BorrowBookRequest;
import com.example.lab2.response.GeneralResponse;
import com.example.lab2.service.BorrowService;
import com.example.lab2.utils.JwtUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Objects;

@RestController
public class BorrowController {
    @Resource(name = "borrowService")
    BorrowService borrowService;


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

        String adminUsername = JwtUtils.getUserName(token);

        //进入业务层
        GeneralResponse generalResponse = borrowService.lendBookToUser(
                borrowBookRequest.getUniqueBookMarkList(),
                borrowBookRequest.getUsername(),
                adminLibraryID,
                adminUsername
        );

        //把结果返回给前端
        return ResponseEntity.ok(generalResponse);

    }


}
