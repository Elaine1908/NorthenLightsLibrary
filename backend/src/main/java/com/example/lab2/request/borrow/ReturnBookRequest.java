package com.example.lab2.request.borrow;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 用户还书的请求
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Valid
public class ReturnBookRequest {
    @NotNull(message = "取的预约书不能为空")
    private List<ReturnSingleBookRequest> uniqueBookMarkList;
}
