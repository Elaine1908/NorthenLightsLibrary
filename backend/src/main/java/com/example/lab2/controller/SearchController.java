package com.example.lab2.controller;


import com.example.lab2.dao.BookTypeRepository;
import com.example.lab2.dao.LibraryRepository;
import com.example.lab2.response.search.GetBookTypeAndCopyResponse;
import com.example.lab2.service.SearchService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;


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


}
