package com.example.lab2.controller;


import com.example.lab2.exception.ReserveException;
import com.example.lab2.request.reserve.ReserveRequest;
import com.example.lab2.response.GeneralResponse;
import com.example.lab2.service.ReserveService;
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
public class ReserveController {


    @Resource(name = "reserveService")
    ReserveService reserveService;

    /**
     * 这个接口是用于处理用户的预约请求的！
     *
     * @param reserveRequest
     * @param bindingResult
     * @param httpServletRequest
     * @return
     */
    @PostMapping("/user/reserveBook")
    public ResponseEntity<GeneralResponse> handleReserve(
            @RequestBody @Valid ReserveRequest reserveRequest,
            BindingResult bindingResult,
            HttpServletRequest httpServletRequest) {

        //如果输入不满足要求就抛出异常给exception handler
        if (bindingResult.hasFieldErrors()) {
            throw new ReserveException(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }

        //获得jwt token
        String token = httpServletRequest.getHeader("token");


        //获得用户名
        String username = JwtUtils.getUserName(token);

        //在service层尝试预约
        GeneralResponse response = reserveService.reserveBook(reserveRequest.getUniqueBookMark(), username);

        //返回给前端
        return ResponseEntity.ok(response);
    }
}

