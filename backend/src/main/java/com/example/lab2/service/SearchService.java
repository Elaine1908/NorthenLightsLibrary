package com.example.lab2.service;

import com.example.lab2.dao.BookRepository;
import com.example.lab2.dto.BookDTO;
import com.example.lab2.entity.Book;
import com.example.lab2.request.search.GetAllBooksRequest;
import com.example.lab2.response.search.GetAllBooksResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public class SearchService {


    public static GetAllBooksResponse getAllBooks(GetAllBooksRequest request, BookRepository bookRepository) {

        //创建请求，去数据库搜索
        PageRequest pageRequest = PageRequest.of(request.getRequestedPage(), request.getPageSize());
        Page<BookDTO> page = bookRepository.getAllBook(pageRequest);

        //将查询的结果包装成response对象
        GetAllBooksResponse response = new GetAllBooksResponse(
                Math.min(request.getRequestedPage(), page.getTotalPages()), page.getTotalPages());

        //更新书本图片文件的content，准备写到json中
        List<BookDTO> bookList = page.getContent();
        for (BookDTO b : bookList) {
            b.setContent();
        }

        response.setBookList(bookList);

        return response;


    }
}
