package com.example.lab2.request.auth;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 设置用户借阅/预约时长和最大副本数量的请求
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Valid
@Data
public class SetUserConfigurationRequest {
    @NotNull(message = "用户种类不能为空")
    private String role;

    @NotNull(message = "最大副本数量不能为空")
    @Pattern(regexp = "^([1-9][0-9]*)$", message = "最大副本数量必须是非负整数")
    private String max_book_borrow;

    @NotNull(message = "最长借阅时间不能为空")
    @Pattern(regexp = "^([1-9][0-9]*)$", message = "最大借阅时间必须是非负整数")
    private String max_borrow_time;

    @NotNull(message = "最长预约时间不能为空")
    @Pattern(regexp = "^([1-9][0-9]*)$", message = "最大预约时间必须是非负整数")
    private String max_reserve_time;
}
