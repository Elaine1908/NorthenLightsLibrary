package com.example.lab2.request.borrow;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 用户取预约书的请求
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Valid
public class BorrowReservedBookRequest {
    @NotNull(message = "用户名不能为空")
    private String username;

    @NotNull(message = "取的预约书不能为空")
    private List<String> uniqueBookMarkList;
}
