package com.example.lab2.response.search;

import com.example.lab2.dto.BookDTO;
import com.example.lab2.response.GeneralResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * lab2，一次性获取所有请求的回复。
 */
@Getter
@Setter
public class GetAllBooksResponse extends GeneralResponse {
    private int actualPage;//事实上，经过服务器处理以后得到的是第几页
    private int totalPage;//一共有几页
    private List<BookDTO> bookList;

    public GetAllBooksResponse(int actualPage, int totalPage) {
        this.actualPage = actualPage;
        this.totalPage = totalPage;
    }
}
