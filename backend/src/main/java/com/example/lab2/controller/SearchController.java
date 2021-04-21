package com.example.lab2.controller;


import com.example.lab2.dao.BookTypeRepository;
import com.example.lab2.dao.LibraryRepository;
import com.example.lab2.entity.BookType;
import com.example.lab2.exception.UploadException;
import com.example.lab2.response.search.GetBookTypeAndCopyResponse;
import com.example.lab2.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;


/**
 * 从数据库中读取书的控制器
 */
@RestController
@CrossOrigin(allowCredentials = "true", originPatterns = "*")
public class SearchController {

    @Autowired
    private BookTypeRepository bookTypeRepository;

    @Autowired
    private LibraryRepository libraryRepository;

    @Resource(name = "searchService")
    private SearchService searchService;

    /**
     * 前端调用这个借口，就能获得所有图书馆的信息
     *
     * @return 图书馆信息
     */
    @GetMapping("/visitor/libraryInfo")
    public HashMap<String, Object> libraryInfo() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("libraryList", libraryRepository.findAll());
        return map;
    }

    /**
     * 根据isbn，得到书的基本信息，每个分管有几本，每个副本的状态
     *
     * @param isbn isbn
     * @return info
     */
    @GetMapping("/useradmin/getBookTypeAndCopy")
    public ResponseEntity<GetBookTypeAndCopyResponse> getBookTypeAndCopy(
            @RequestParam(name = "isbn") String isbn) {
        return ResponseEntity.ok(
                searchService.getBookTypeAndCopy(isbn)
        );
    }


    /**
     * 用户根据isbn或作者或书名搜索书（bookType）的接口
     *
     * @param isbn     isbn
     * @param author   作者（精确搜索）
     * @param bookName 书名（模糊搜索）
     * @return info
     */
    @GetMapping("/useradmin/getBookType")
    public ResponseEntity<HashMap<String, List<BookType>>> getBookType(
            @RequestParam(name = "isbn", required = false) String isbn,
            @RequestParam(name = "author", required = false) String author,
            @RequestParam(name = "bookName", required = false) String bookName
    ) {
        if (isbn == null && author == null && bookName == null) {
            throw new IllegalArgumentException("isbn，作者和书名至少应输入一个");
        }

        //新建respnse的hashmap
        HashMap<String, List<BookType>> response = new HashMap<>();

        response.put("bookTypeList", searchService.getBookType(isbn, author, bookName));

        //把请求返回给前端
        return ResponseEntity.ok(response);


    }


}
