package com.example.lab2.controller;

import com.example.lab2.dto.UserDTO;
import com.example.lab2.entity.UserConfiguration;
import com.example.lab2.exception.auth.RegisterException;
import com.example.lab2.exception.auth.SetConfigurationException;
import com.example.lab2.request.auth.AddAdminRequest;
import com.example.lab2.request.auth.DeleteAdminRequest;
import com.example.lab2.request.auth.SetUserConfigurationRequest;
import com.example.lab2.service.UserConfigurationService;
import com.example.lab2.service.UserDetailsServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
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
    public ResponseEntity<HashMap<String, List<UserConfiguration>>> getAllUserConfiguration() {
        HashMap<String, List<UserConfiguration>> res = new HashMap<>();
        res.put("userConfigurationList",
                userConfigurationService.getAllUserConfiguration());
        return ResponseEntity.ok(res);

    }

    /**
     * 设置所有种类用户的最长借书时间，最长预约时间和最大借书数量
     *  @return
     *  @author yiwen
     */
    @PostMapping("/setUserConfiguration")
    public ResponseEntity<HashMap<String, String>> setUserConfiguration(@RequestBody @Valid SetUserConfigurationRequest setUserConfigurationRequest
            , BindingResult bindingResult){

        //如果从前端接口传来的信息存在不合法参数
        if (bindingResult.hasFieldErrors()) {
            throw new SetConfigurationException(
                    Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage()
            );
        }


        HashMap<String, String> map = userConfigurationService.setUserConfiguration(setUserConfigurationRequest);

        return ResponseEntity.ok(map);
    }
}


