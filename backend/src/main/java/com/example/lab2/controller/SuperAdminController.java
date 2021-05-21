package com.example.lab2.controller;

import com.example.lab2.dto.UserConfigurationDTO;
import com.example.lab2.dto.UserDTO;
import com.example.lab2.entity.UserConfiguration;
import com.example.lab2.exception.auth.RegisterException;
import com.example.lab2.exception.auth.SetConfigurationException;
import com.example.lab2.request.auth.AddAdminRequest;
import com.example.lab2.request.auth.DeleteAdminRequest;
import com.example.lab2.request.auth.SetUserConfigurationRequest;
import com.example.lab2.service.EmailService;
import com.example.lab2.service.UserConfigurationService;
import com.example.lab2.service.UserDetailsServiceImpl;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/superadmin")
public class SuperAdminController {

    @Resource(name = "userService")
    UserDetailsServiceImpl userDetailsService;

    @Resource(name = "userConfigurationService")
    UserConfigurationService userConfigurationService;

    @Resource(name = "emailService")
    EmailService emailService;

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


    @PostMapping("/deleteAdmin")
    public ResponseEntity<?> deleteAdmin(@RequestBody @Valid DeleteAdminRequest deleteAdminRequest, BindingResult bindingResult) {
        //如果从前端接口传来的信息存在不合法参数
        if (bindingResult.hasFieldErrors()) {
            throw new RegisterException(
                    Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage()
            );
        }
        return ResponseEntity.ok(userDetailsService.deleteAdmin(deleteAdminRequest));
    }

    @RequestMapping("/showAdmin")
    public ResponseEntity<?> showAdmin() {
        List<UserDTO> admins = userDetailsService.showAdmin();
        HashMap<String, Object> result = new HashMap<>();
        result.put("admin", admins);
        return ResponseEntity.ok(result);
    }

    /**
     * 获得所有种类用户的最长借书时间，最长预约时间和最大借书数量
     *
     * @return
     * @author haojie
     */
    @GetMapping("/userConfiguration")
    public ResponseEntity<HashMap<String, List<UserConfigurationDTO>>> getAllUserConfiguration() {
        HashMap<String, List<UserConfigurationDTO>> res = new HashMap<>();
        res.put("userConfigurationList",
                userConfigurationService.getAllUserConfigurationDTO());
        return ResponseEntity.ok(res);

    }

    /**
     * 设置所有种类用户的最长借书时间，最长预约时间和最大借书数量
     *
     * @return
     * @author yiwen
     */
    @PostMapping("/setUserConfiguration")
    public ResponseEntity<HashMap<String, String>> setUserConfiguration(@RequestBody @Valid SetUserConfigurationRequest setUserConfigurationRequest
            , BindingResult bindingResult) {

        //如果从前端接口传来的信息存在不合法参数
        if (bindingResult.hasFieldErrors()) {
            throw new SetConfigurationException(
                    Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage()
            );
        }

        if (setUserConfigurationRequest.getMaxBorrowTime().getTotalSecs() <= 0) {
            throw new IllegalArgumentException("请检查最大借阅时间是否输入正确");
        }

        if (setUserConfigurationRequest.getMaxReserveTime().getTotalSecs() <= 0) {
            throw new IllegalArgumentException("请检查最大借阅时间是否输入正确");
        }

        //将前端传来的天，小时，分，秒统统转换为秒
        setUserConfigurationRequest.setMax_borrow_time(
                setUserConfigurationRequest.getMaxBorrowTime().getTotalSecs() + ""
        );

        setUserConfigurationRequest.setMax_reserve_time(
                setUserConfigurationRequest.getMaxReserveTime().getTotalSecs() + ""
        );


        HashMap<String, String> map = userConfigurationService.setUserConfiguration(setUserConfigurationRequest);

        return ResponseEntity.ok(map);
    }

    /**
     * 用户预约超期，借阅超期，罚款未缴纳邮件提醒
     *
     * @return
     * @author yiwen
     */
    @PostMapping("/notify")
    public ResponseEntity<?> notify(@RequestBody JSONObject jsonObject) throws Exception {
        String type = jsonObject.get("messages").toString();
        HashMap<String, String> map = emailService.sendNotify(type);
        return ResponseEntity.ok(map);
    }
}


