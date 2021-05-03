package com.example.lab2.controller;

import com.example.lab2.dao.UserRepository;
import com.example.lab2.entity.User;
import com.example.lab2.exception.auth.RegisterException;
import com.example.lab2.request.auth.ChangePasswordRequest;
import com.example.lab2.request.auth.RegisterRequest;
import com.example.lab2.response.GeneralResponse;
import com.example.lab2.service.UserDetailsServiceImpl;
import com.example.lab2.utils.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
public class UserController {
    @Autowired
    private UserRepository userRepository;
    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Resource(name = "userService")
    UserDetailsServiceImpl userDetailsService;


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
            userRepository.save(user);
        } catch (Exception e) {
            throw new RegisterException("注册失败，你的用户名可能与他人的重了。请换个用户名再试");
        }
        return ResponseEntity.ok(new GeneralResponse("注册成功！"));

    }

    /**
     * 用户修改密码的接口，从jwt中读取用户名
     *
     * @return
     * @author zhj
     */
    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(
            @RequestBody @Valid ChangePasswordRequest changePasswordRequest,
            BindingResult bindingResult,
            HttpServletRequest httpServletRequest
    ) {

        //如果输入参数不正确，就抛出异常！
        if (bindingResult.hasFieldErrors()) {
            throw new IllegalArgumentException(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }

        //获得原密码与现密码
        String originalPassword = changePasswordRequest.getOriginalPassword();
        String newPassword = changePasswordRequest.getNewPassword();

        //根据token获得用户名
        String token = httpServletRequest.getHeader("token");
        String username = JwtUtils.getUserName(token);

        GeneralResponse response = userDetailsService.changePassword(originalPassword, newPassword, username);

        //把结果返回给前端
        return ResponseEntity.ok(response);

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
