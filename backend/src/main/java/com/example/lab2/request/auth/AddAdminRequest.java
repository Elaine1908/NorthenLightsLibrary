package com.example.lab2.request.auth;


import jdk.jfr.Name;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


/**
 * 添加管理员的请求
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Valid
@Data
public class AddAdminRequest {

    @NotNull(message = "用户名不能为空")
    private String username;

    @NotNull(message = "密码不能为空")
    @Pattern(regexp = "[a-zA-Z0-9-_]{6,32}", message = "密码必须是字⺟，数字或者特殊字符（-_）⾄少包含两种")
    private String password;

    @NotNull(message = "邮箱不能为空")
    @Email(message = "邮箱格式不符合要求")
    private String email;



}
