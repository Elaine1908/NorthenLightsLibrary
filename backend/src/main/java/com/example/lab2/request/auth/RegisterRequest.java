package com.example.lab2.request.auth;


import com.example.lab2.entity.User;
import com.example.lab2.exception.RegisterException;
import lombok.*;
import org.springframework.util.DigestUtils;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.nio.charset.StandardCharsets;

/**
 * lab2注册的请求
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Valid
@Data
public class RegisterRequest {
    @Pattern(regexp = "[0-9]{2}(30|21|11)[0-9]{3}[0-9]{4}", message = "用户名必须是有效的学号！")
    @NotNull(message = "用户名不能为空")
    private String username;//用户名

    @Pattern(regexp = "[a-zA-Z0-9-_]{6,32}", message = "密码必须是字⺟，数字或者特殊字符（-_）⾄少包含两种")
    @NotNull(message = "密码不能为空")
    private String passWord;

    @NotNull(message = "确认密码不能为空")
    private String passWordAgain;

    private static String emailSuffix = "@fudan.edu.cn";

    /**
     * 检查两次密码是否输入一致，密码是否包含了字母，数字，特殊字符的至少两种
     */
    public void checkPassword() {
        if (!this.passWord.equals(passWordAgain)) {
            throw new RegisterException("两次密码输入不一致");
        }
        if (!checkPasswordStrength()) {
            throw new RegisterException("密码中字母，数字，特殊字符必须包含至少两种。");
        }
        if (this.passWord.contains(this.username)) {
            throw new RegisterException("密码中不能包含帐号！");
        }
    }

    /**
     * 密码是否包含了字母，数字，特殊字符的至少两种
     * 其实这个可以用注解+正则表达式实现，先硬编码一下，以后再说
     *
     * @return 如果满足要求，返回true。否则返回false
     */
    private boolean checkPasswordStrength() {

        boolean[] bool = new boolean[3];
        int cnt = 0;
        for (int i = 0; i < passWord.length(); i++) {
            char c = passWord.charAt(i);
            if (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z') {
                bool[0] = true;
            } else if (c >= '0' && c <= '9') {
                bool[1] = true;
            } else if (c == '-' || c == '_') {
                bool[2] = true;
            }
        }
        for (boolean b : bool) {
            if (b) {
                cnt += 1;
            }
        }
        return cnt >= 2;
    }

    /**
     * 根据RegisterRequest中的信息，得到一个User对象
     *
     * @return User对象
     */
    public User createUserObject() {

        //先默认创建为学生，权限的事情以后再说。
        return new User(username, passWord, username + emailSuffix, "student", 100);
    }


}
