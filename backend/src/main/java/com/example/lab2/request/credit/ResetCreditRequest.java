package com.example.lab2.request.credit;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@Valid
public class ResetCreditRequest {

    @NotNull(message = "用户名不能为空")
    private String username;

    @NotNull(message = "要设置成的信用不能为空")
    private Integer to;


}
