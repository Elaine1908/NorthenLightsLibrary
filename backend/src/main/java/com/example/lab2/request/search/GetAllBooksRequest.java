package com.example.lab2.request.search;

import lombok.Getter;
import lombok.Setter;


/**
 * lab2,一次性获取数据库中所有数据的请求
 */
@Getter
@Setter
public class GetAllBooksRequest {
    private int requestedPage;//请求的是第几页的书
    private int pageSize;//每一页上几个条目
}
