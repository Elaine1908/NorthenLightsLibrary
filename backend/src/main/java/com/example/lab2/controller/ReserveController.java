package com.example.lab2.controller;


import com.example.lab2.dto.ReservedBookCopyDTO;
import com.example.lab2.request.reserve.ReserveRequest;
import com.example.lab2.response.GeneralResponse;
import com.example.lab2.service.ReserveService;
import com.example.lab2.utils.JwtUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
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
            throw new IllegalArgumentException(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
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

    /**
     * 现场取预约过的书籍，显示一个用户预约过的所有书籍
     *
     * @return
     */
    @GetMapping("/admin/showReservation")
    public ResponseEntity<HashMap<String, Object>> getAllReservation(
            @RequestParam("username")String username) {


        //获得预约过的书
        List<ReservedBookCopyDTO> reservedBookCopyDTOS = reserveService.getAllReservation(username);

        //加入result
        HashMap<String, Object> result = new HashMap<>();
        result.put("reservedBooks", reservedBookCopyDTOS);

        //返回给前端
        return ResponseEntity.ok(result);


    }
}

