package com.example.lab2.request.borrow;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * 用户归还单本书的请求
 * @author zhj
 */
@Getter
@Setter
@NoArgsConstructor
public class ReturnSingleBookRequest {
    private String uniqueBookMark;
    private String status;

    public ReturnSingleBookRequest(String uniqueBookMark, String status) {
        this.uniqueBookMark = uniqueBookMark;
        this.status = status;
    }
}
