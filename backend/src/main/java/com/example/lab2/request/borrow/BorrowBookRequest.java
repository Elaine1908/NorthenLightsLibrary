package com.example.lab2.request.borrow;

import jdk.jfr.Name;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * 用户借书的请求
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Valid
public class BorrowBookRequest {

    @NotNull(message = "用户名不能为空")
    private String username;

    @NotNull(message = "借书的标识不能为空")
    private String uniqueBookMark;


}
