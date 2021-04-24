package com.example.lab2.request.reserve;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * 显示用户所有预约的书籍时，发来的请求对象
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Valid
public class ShowReservationRequest {

    @NotNull(message = "用户名不能为空！")
    private String username;

}
