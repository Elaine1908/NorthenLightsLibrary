package com.example.lab2.request.auth;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Valid
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SendEmailCaptchaRequest {

    @NotNull
    @Email(message = "邮箱格式错误")
    private String email;
}
