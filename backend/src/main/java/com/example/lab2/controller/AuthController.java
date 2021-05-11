package com.example.lab2.controller;

import com.example.lab2.dao.UserRepository;
import com.example.lab2.entity.User;
import com.example.lab2.exception.auth.CaptchaNotCorrectException;
import com.example.lab2.exception.auth.RegisterException;
import com.example.lab2.exception.auth.UserAlreadyExistException;
import com.example.lab2.request.auth.ChangePasswordRequest;
import com.example.lab2.request.auth.RegisterRequest;
import com.example.lab2.request.auth.SendEmailCaptchaRequest;
import com.example.lab2.response.GeneralResponse;
import com.example.lab2.service.EmailService;
import com.example.lab2.service.UserDetailsServiceImpl;
import com.example.lab2.utils.JwtUtils;
import org.hibernate.validator.internal.engine.messageinterpolation.parser.MessageDescriptorFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;


/**
 * 这是和用户登陆注册有关的controller
 */
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserRepository userRepository;
    Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Resource(name = "userService")
    UserDetailsServiceImpl userDetailsService;

    @Resource(name = "emailService")
    EmailService emailService;


    @PostMapping("/register")
    public ResponseEntity<GeneralResponse> tryRegister(@RequestBody @Valid RegisterRequest request,
                                                       BindingResult bindingResult,
                                                       HttpServletRequest httpServletRequest) {
        if (bindingResult.hasFieldErrors()) {
            throw new RegisterException(
                    Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage()
            );
        }

        //从session中读取出验证码来
        HttpSession session = httpServletRequest.getSession();
        Object sessionCaptcha = session.getAttribute("captcha");
        Object sessionEmail = session.getAttribute("email");

        //检查验证码
        if (!request.getCaptcha().equals(sessionCaptcha) || !request.getEmail().equals(sessionEmail)) {
            logger.info(String.format("用户名%s想要注册，但是因为验证码错误被拒绝了", request.getUsername()));
            throw new CaptchaNotCorrectException("验证码错误！");
        }

        logger.debug("RegisterForm: " + request.toString());
        request.checkPassword();

        //得到user对象
        User user = request.createUserObject();
        try {
            userRepository.save(user);
        } catch (Exception e) {
            logger.info(String.format("用户名%s想要注册，但是因为存在同名用户被拒绝了", request.getUsername()));
            throw new RegisterException("注册失败，你的用户名可能与他人的重了。请换个用户名再试");
        }

        //去掉session中的验证码，即一个验证码只能用一次

        logger.info(String.format("用户名%s注册成功", request.getUsername()));
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


    /**
     * 给用户的邮箱发邮件的rest接口
     *
     * @param sendEmailCaptchaRequest
     * @param httpServletRequest
     * @return
     * @throws MessagingException
     * @author haojie
     */
    @PostMapping("/sendEmailCaptcha")
    public ResponseEntity<GeneralResponse> sendRegisterEmailCaptcha(
            @RequestBody @Valid SendEmailCaptchaRequest sendEmailCaptchaRequest, HttpServletRequest httpServletRequest
    ) throws MessagingException {

        String email = sendEmailCaptchaRequest.getEmail();

        //先看看用户存不存在，否则就不用发验证码了
        Optional<User> user = userRepository.findUserByEmail(email);

        if (user.isPresent()) {
            logger.info(String.format("想要给邮箱%s发注册验证码，但这个邮箱已经被注册了", email));
            throw new UserAlreadyExistException("该邮箱已经被注册了");
        }


        //发邮件的动作
        String captcha = emailService.sendRegisterCaptcha(email);
        logger.info(String.format("给邮箱%s发去了验证码%s", email, captcha));

        //在session中设置发出的验证码，以便之后校对
        HttpSession httpSession = httpServletRequest.getSession();
        httpSession.setAttribute("email", email);
        httpSession.setAttribute("captcha", captcha);

        return ResponseEntity.ok(
                new GeneralResponse(String.format("发送验证码到%s成功", email))
        );

    }

}
