package com.example.lab2.controller;

import com.example.lab2.entity.Fine;
import com.example.lab2.service.FineService;
import com.example.lab2.service.NormalUserService;
import com.example.lab2.utils.JwtUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/user")
public class NormalUserController {

    @Resource(name = "normalUserService")
    NormalUserService normalUserService;

    @Resource(name = "fineService")
    FineService fineService;

    @GetMapping("/userinfo")
    public ResponseEntity<?> userInfo(HttpServletRequest request) {
        String token = request.getHeader("token");
        String username = JwtUtils.getUserName(token);
        return ResponseEntity.ok(normalUserService.userInfo(username));
    }


    /**
     * 得到用户的全部罚款
     *
     * @return
     * @author zhj
     */
    @GetMapping("/myfine")
    public ResponseEntity<HashMap<String, List<Fine>>> getMyFine(HttpServletRequest httpServletRequest) {

        //在header中获得token，再从token中解析出用户名
        String token = httpServletRequest.getHeader("token");
        String username = JwtUtils.getUserName(token);

        List<Fine> fineList = fineService.getAllFine(username);

        HashMap<String, List<Fine>> res = new HashMap<>();
        res.put("fineList", fineList);
        return ResponseEntity.ok(res);

    }


}
