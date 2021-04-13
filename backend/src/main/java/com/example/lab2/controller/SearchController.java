package com.example.lab2.controller;


import com.example.lab2.dao.BookTypeRepository;
import com.example.lab2.dao.LibraryRepository;
import com.example.lab2.entity.Library;
import com.example.lab2.request.search.GetAllBooksRequest;
import com.example.lab2.response.search.GetAllBooksResponse;
import com.example.lab2.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;


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


}
