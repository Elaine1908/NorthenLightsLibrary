package com.example.lab2.request.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
@Valid
@AllArgsConstructor
public class ChangePasswordRequest {

    @NotNull(message = "原密码不能为空")
    private String originalPassword;

    @Pattern(regexp = "[a-zA-Z0-9-_]{6,32}", message = "新密码必须是字⺟，数字或者特殊字符（-_）⾄少包含两种")
    @NotNull(message = "新密码不能为空")
    private String newPassword;


}
