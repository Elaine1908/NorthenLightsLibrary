package com.example.lab2.controller;


import com.example.lab2.entity.User;
import com.example.lab2.exception.RegisterException;
import com.example.lab2.request.auth.AddAdminRequest;
import com.example.lab2.service.UserDetailsServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.jws.soap.SOAPBinding;
import javax.swing.text.html.Option;
import javax.validation.Valid;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Objects;

@RestController
@RequestMapping("/superadmin")
public class SuperAdminController {

    @Resource(name = "userService")
    UserDetailsServiceImpl userDetailsService;

    /**
     * 添加管理员的接口
     *
     * @param addAdminRequest 添加管理员的请求
     * @param bindingResult   检测输入是否合法
     */
    @PostMapping("/addAdmin")
    public ResponseEntity<HashMap<String, String>> addAdmin(@RequestBody @Valid AddAdminRequest addAdminRequest
            , BindingResult bindingResult) {

        //如果从前端接口传来的信息存在不合法参数
        if (bindingResult.hasFieldErrors()) {
            throw new RegisterException(
                    Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage()
            );
        }

        //在service层中尝试添加
        HashMap<String, String> map = userDetailsService.addAdmin(addAdminRequest);

        //把结果发回给前端
        return ResponseEntity.ok(map);

    }

}


