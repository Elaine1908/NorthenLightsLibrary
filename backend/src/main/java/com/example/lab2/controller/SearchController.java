package com.example.lab2.controller;


import com.example.lab2.dao.BookTypeRepository;
import com.example.lab2.request.search.GetAllBooksRequest;
import com.example.lab2.response.search.GetAllBooksResponse;
import com.example.lab2.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * 从数据库中读取书的控制器
 */
@RestController
@RequestMapping("/search")
@CrossOrigin(allowCredentials = "true",originPatterns = "*")
public class SearchController {

    @Autowired
    private BookTypeRepository bookTypeRepository;



}
