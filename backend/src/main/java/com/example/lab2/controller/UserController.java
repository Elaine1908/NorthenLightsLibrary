package com.example.lab2.controller;

import com.example.lab2.entity.User;
import com.example.lab2.exception.RegisterException;
import com.example.lab2.request.auth.RegisterRequest;
import com.example.lab2.response.GeneralResponse;
import com.example.lab2.service.UserDetailsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import org.springframework.dao.DataIntegrityViolationException;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * 这是和用户登陆注册有关的controller
 */
@RestController
@RequestMapping("/auth")
@CrossOrigin(allowCredentials = "true", originPatterns = "*")
public class UserController {
    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;
    Logger logger = LoggerFactory.getLogger(UserController.class);


    @PostMapping("/register")
    public ResponseEntity<GeneralResponse> tryRegister(@RequestBody @Valid RegisterRequest request, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            throw new RegisterException(
                    Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage()
            );
        }

        logger.debug("RegisterForm: " + request.toString());
        request.checkPassword();
        //得到user对象
        User user = request.createUserObject();
        try {
            UserDetailsServiceImpl.save(user);
        } catch (DataIntegrityViolationException | SQLIntegrityConstraintViolationException e) {
            throw new RegisterException("注册失败，你的用户名可能与他人的重了。请换个用户名再试");
        }
        return ResponseEntity.ok(new GeneralResponse("注册成功！"));

    }

    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestParam("newPassword")String newPassword,@RequestParam("username")String username) {
        userDetailsServiceImpl.changePassword(newPassword,username);
        return ResponseEntity.ok(new GeneralResponse("密码更新成功！"));
    }





    /**
     * This is a function to test your connectivity. (健康测试时，可能会用到它）.
     */
    @GetMapping("/welcome")
    public ResponseEntity<?> welcome() {
        Map<String, String> response = new HashMap<>();
        String message = "Welcome to 2021 Software Engineering Lab2. ";
        response.put("message", message);
        return ResponseEntity.ok(response);
    }

}
