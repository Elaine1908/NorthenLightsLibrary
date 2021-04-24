package com.example.lab2.controller;

import com.example.lab2.service.NormalUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
@CrossOrigin(allowCredentials = "true", originPatterns = "*")
public class NormalUserController {

    @Resource(name = "normalUserService")
    NormalUserService normalUserService;

    @GetMapping("/userinfo")
    public ResponseEntity<?> userInfo(@RequestParam("username")String username){
        return ResponseEntity.ok(normalUserService.userInfo(username));
    }
}
