package com.example.lab2.request.auth;

import com.example.lab2.dto.TimeDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.sql.Time;

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
    @JsonProperty("maxBookBorrow")
    private String max_book_borrow;

    //下面两个参数是本来的接受参数。但因为前端想要传天小时分秒这样的格式，因此下面两个现在不用来传参数
    //而是在controller将TimeDTO转换成下面这两个参数，这样别的代码就不用动
    private String max_borrow_time;

    private String max_reserve_time;

    private TimeDTO maxBorrowTime;

    private TimeDTO maxReserveTime;

    public SetUserConfigurationRequest(@NotNull(message = "用户种类不能为空") String role, @NotNull(message = "最大副本数量不能为空") @Pattern(regexp = "^([1-9][0-9]*)$", message = "最大副本数量必须是非负整数") String max_book_borrow, String max_borrow_time, String max_reserve_time) {
        this.role = role;
        this.max_book_borrow = max_book_borrow;
        this.max_borrow_time = max_borrow_time;
        this.max_reserve_time = max_reserve_time;
    }
}
