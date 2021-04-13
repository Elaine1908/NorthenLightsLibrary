package com.example.lab2.controller;

import com.example.lab2.dto.UserTDO;
import com.example.lab2.entity.User;
import com.example.lab2.exception.RegisterException;
import com.example.lab2.request.auth.LoginRequest;
import com.example.lab2.request.auth.RegisterRequest;
import com.example.lab2.response.GeneralResponse;
import com.example.lab2.service.UserDetailsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.dao.DataIntegrityViolationException;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@RestController
@RequestMapping("/auth")
@CrossOrigin(allowCredentials = "true", originPatterns = "*")
public class UserController {
    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;
    Logger logger = LoggerFactory.getLogger(UserController.class);

    //当前端使用application/json来传递数据的时候，后端只能使用 @RequestBody 以及 Java bean或者 map 的方式来接收数据。
    //@GetMapping : ResponseBody注解
    //@PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpSession session) {// 待修改 因为get发送数据的方式不是json
        logger.debug("LoginForm: " + request.toString());
        User user = userDetailsServiceImpl.login(request.getUsername(), request.getPassword());
        session.setAttribute("user", user);
        return ResponseEntity.ok(new GeneralResponse("欢迎" + user.getUsername()));
    }

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
            userDetailsServiceImpl.save(user);
        } catch (DataIntegrityViolationException | SQLIntegrityConstraintViolationException e) {
            throw new RegisterException("注册失败，你的用户名可能与他人的重了。请换个用户名再试");
        }
        return ResponseEntity.ok(new GeneralResponse("注册成功！"));
    }

    /**
     * 尝试自动登陆。如果session里面有user对象，就给前端返回user对象的用户名，否则返回403 Forbidden
     *
     * @param session session
     * @return 200/403
     */
    @PostMapping("/autologin")
    public ResponseEntity<UserTDO> tryAutoLogin(HttpSession session) {
        UserTDO userTDO = new UserTDO();
        User user = (User) session.getAttribute("user");
        if (user != null) {
            userTDO.setUsername(user.getUsername());
            return ResponseEntity.ok(userTDO);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);

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
