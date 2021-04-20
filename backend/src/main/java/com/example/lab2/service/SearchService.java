package com.example.lab2.service;

import com.example.lab2.dao.*;
import com.example.lab2.dao.BookTypeRepository;
import com.example.lab2.dao.LibraryRepository;
import com.example.lab2.entity.BookCopy;
import com.example.lab2.entity.BookType;
import com.example.lab2.entity.Library;
import com.example.lab2.exception.BookTypeNotFoundException;
import com.example.lab2.response.search.GetBookTypeAndCopyResponse;
import com.example.lab2.response.search.NumberToLibrary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service("searchService")
public class SearchService {


    @Autowired
    private LibraryRepository libraryRepository;

    @Autowired
    private BookCopyRepository bookCopyRepository;

    @Autowired
    private BookTypeRepository bookTypeRepository;

    /**
     * 根据isbn，读取书的信息和每个副本的信息，每个分管有几本
     *
     * @param isbn isbn
     * @return info
     */
    public GetBookTypeAndCopyResponse getBookTypeAndCopy(String isbn) {

        //先根据isbn得到booktype
        Optional<BookType> bookType = bookTypeRepository.getBookTypeByISBN(isbn);

        //检查这一种booktype是否存在
        if (!bookType.isPresent()) {
            throw new BookTypeNotFoundException("在本馆中找不到这种书，请重试");
        }

        //先根据booktype创建response对象
        GetBookTypeAndCopyResponse getBookTypeAndCopyResponse =
                new GetBookTypeAndCopyResponse(bookType.get());

        //获得所有的bookcopy
        List<BookCopy> bookCopies = bookCopyRepository.getBookCopiesByISBN(isbn);

        //在response对象中设置bookcopies
        getBookTypeAndCopyResponse.setBookCopies(bookCopies);

        //图书馆列表
        List<Library> libraries = libraryRepository.findAll();

        //得到每个图书馆有多少本的列表
        List<NumberToLibrary> numberEachLibrary = this.getNumberEachLibrary(bookCopies, libraries);

        //设置每个图书馆有几本书的列表
        getBookTypeAndCopyResponse.setNumberEachLibrary(numberEachLibrary);

        return getBookTypeAndCopyResponse;
    }

    /**
     * 得到每个图书馆有几本的列表
     *
     * @param bookCopies 图书副本列表
     * @param libraries  图书馆列表
     * @return 每个图书馆有几本？
     */
    private List<NumberToLibrary> getNumberEachLibrary(List<BookCopy> bookCopies, List<Library> libraries) {
        //每个图书馆有几种这本书的副本
        List<NumberToLibrary> numberEachLibrary = new ArrayList<>();

        //从图书馆id到numberEachLibrary的映射
        HashMap<Long, NumberToLibrary> libraryIDToNode = new HashMap<>();

        //初始化
        libraries.forEach((library) -> {
            NumberToLibrary numberToLibrary = new NumberToLibrary(library.getLibraryName(), 0);
            numberEachLibrary.add(numberToLibrary);
            libraryIDToNode.put(library.getLibraryID(), numberToLibrary);
        });

        //根据bookcopylist更新每个图书馆有几本书的信息
        bookCopies.forEach((bookCopy -> {
            libraryIDToNode.get(bookCopy.getLibraryID()).numberIncrement();

        }));

        return numberEachLibrary;

    }

}
