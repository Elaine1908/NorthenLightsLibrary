package com.example.lab2.controller;

import com.example.lab2.service.NormalUserService;
import com.example.lab2.utils.JwtUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class NormalUserController {

    @Resource(name = "normalUserService")
    NormalUserService normalUserService;

    @GetMapping("/userinfo")
    public ResponseEntity<?> userInfo(HttpServletRequest request) {
        String token = request.getHeader("token");
        String username = JwtUtils.getUserName(token);
        return ResponseEntity.ok(normalUserService.userInfo(username));
    }
}
