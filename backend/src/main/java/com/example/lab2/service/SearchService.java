package com.example.lab2.service;

import com.example.lab2.dao.*;
import com.example.lab2.dao.BookTypeRepository;
import com.example.lab2.dao.LibraryRepository;
import com.example.lab2.dto.bookcopy.BookCopyDTO;
import com.example.lab2.dto.bookcopy.ShowBookCopyDTO;
import com.example.lab2.entity.BookType;
import com.example.lab2.entity.Comment;
import com.example.lab2.entity.Library;
import com.example.lab2.exception.notfound.BookCopyNotFoundException;
import com.example.lab2.exception.notfound.BookTypeNotFoundException;
import com.example.lab2.response.search.GetBookTypeAndCopyResponse;
import com.example.lab2.response.search.NumberToLibrary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service("searchService")
public class SearchService {


    @Autowired
    private LibraryRepository libraryRepository;

    @Autowired
    private BookCopyRepository bookCopyRepository;

    @Autowired
    private BookTypeRepository bookTypeRepository;

    @Autowired
    private CommentRepository commentRepository;

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
        List<BookCopyDTO> bookCopies = bookCopyRepository.getBookCopiesByISBN(isbn);

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
     * 根据isbn，author，和bookName得到所有的bookType
     *
     * @param isbn     bookType的isbn（精确搜索）
     * @param author   bookType的author（精确搜索）
     * @param bookName bookType的名字（模糊搜索）
     * @return List<BookType>
     */
    public List<BookType> getBookType(String isbn, String author, String bookName) {

        //根据isbn查到的BookType的List
        List<BookType> isbnList = bookTypeRepository.getAllBookTypeByISBN(isbn);

        //根据author查到的BookType的List
        List<BookType> authorList = bookTypeRepository.getAllBookTypeByAuthorFuzzySearch(author);

        //根据bookName查到的BookType的List
        List<BookType> bookNameList = bookTypeRepository.getAllBookTypeByNameFuzzySearch(bookName);

        TreeSet<BookType> set = new TreeSet<>();
        set.addAll(isbnList);
        set.addAll(authorList);
        set.addAll(bookNameList);

        //用set取交集
        if (isbn != null && !isbn.isBlank()) {
            set.retainAll(isbnList);
        }
        if (author != null && !author.isBlank()) {
            set.retainAll(authorList);
        }
        if (bookName != null && !bookName.isBlank()) {
            set.retainAll(bookNameList);
        }

        //把书的列表注入平均分
        set.forEach(bookType -> {
            //从数据库查平均分
            double averageRate = commentRepository.getAverageRateByISBN(bookType.getIsbn());
            bookType.injectAverageRate(averageRate);
        });

        //再转回list
        return new ArrayList<>(set);
    }


    public List<BookType> getAllBookType() {
        //得到所有BookType的列表
        List<BookType> bookTypeList = bookTypeRepository.findAll();

        //注入平均分
        bookTypeList.forEach(bookType -> {
            double averageRate = commentRepository.getAverageRateByISBN(bookType.getIsbn());
            bookType.injectAverageRate(averageRate);
        });

        return bookTypeList;
    }

    /**
     * 得到每个图书馆有几本的列表
     *
     * @param bookCopies 图书副本列表
     * @param libraries  图书馆列表
     * @return 每个图书馆有几本？
     */
    private List<NumberToLibrary> getNumberEachLibrary(List<BookCopyDTO> bookCopies, List<Library> libraries) {
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

    /**
     * 场景：现场借书
     * 此方法用于管理员每输入一个副本号，并在前端网页点击“加号”按钮时，后端从数据库搜索书本并返回
     * 以便在网页动态展示书本形态和信息，让读者确认是否是ta想要借的书
     *
     * @param isbn 副本的唯一标识
     * @return
     */
    public ShowBookCopyDTO getBookCopyByIsbn(String isbn) {
        Optional<ShowBookCopyDTO> bookCopy = bookCopyRepository.getBookCopyByUniqueBookMarkAndShow(isbn);
        if (!bookCopy.isPresent()) {
            throw new BookCopyNotFoundException("该图书的副本没有找到！");
        }
        return bookCopy.orElse(null);
    }
}
