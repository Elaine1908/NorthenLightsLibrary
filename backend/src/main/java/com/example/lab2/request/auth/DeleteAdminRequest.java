package com.example.lab2.request.auth;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * 删除管理员的请求
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Valid
@Data
public class DeleteAdminRequest {
    @NotNull(message = "用户名不能为空")
    private String username;
}
