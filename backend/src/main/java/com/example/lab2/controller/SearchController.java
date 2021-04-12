package com.example.lab2.controller;


import com.example.lab2.dao.BookRepository;
import com.example.lab2.entity.Book;
import com.example.lab2.request.search.GetAllBooksRequest;
import com.example.lab2.response.search.GetAllBooksResponse;
import com.example.lab2.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    private BookRepository bookRepository;

    @GetMapping("/getall")
    public ResponseEntity<GetAllBooksResponse> getAllBooks(
            @RequestParam("requestedPage") int requestedPage,
            @RequestParam("pageSize") int pageSize
    ) {

        GetAllBooksRequest request = new GetAllBooksRequest();
        request.setPageSize(pageSize);
        request.setRequestedPage(requestedPage);

        GetAllBooksResponse response = SearchService.getAllBooks(request, bookRepository);

        return ResponseEntity.ok(response);
    }


}
