package com.example.lab2.service;

import com.example.lab2.entity.BookCopy;
import com.example.lab2.entity.BookType;
import com.example.lab2.entity.Library;
import com.example.lab2.exception.notfound.BookTypeNotFoundException;
import com.example.lab2.request.upload.AddBookCopyRequest;
import com.example.lab2.request.upload.UploadNewBookRequest;
import com.example.lab2.response.search.GetBookTypeAndCopyResponse;
import com.example.lab2.response.search.NumberToLibrary;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

import java.io.FileInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.example.lab2.dao.*;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SearchServiceTest {


    @Resource(name = "searchService")
    SearchService searchService;

    @Resource(name = "uploadService")
    UploadService uploadService;

    @Autowired
    BookkCopyRepository bookCopyRepository;

    @Test
    public void testGetNumberEachLibrary() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        //这是对searchService中得到每个分管有多少本书的方法的测试。由于该方法是私有的，所以只能用反射调用了。。。。。。。
        Method getNumberEachLibrary = searchService.getClass().getDeclaredMethod("getNumberEachLibrary", List.class, List.class);
        getNumberEachLibrary.setAccessible(true);

        List<Library> libraries = new ArrayList<>();
        libraries.add(new Library(1, "1号"));
        libraries.add(new Library(2, "2号"));
        libraries.add(new Library(3, "3号"));

        List<BookCopy> bookCopies = new ArrayList<>();
        bookCopies.add(new BookCopy("", "", "", (long) 1, null, null, (long) 0));
        bookCopies.add(new BookCopy("", "", "", (long) 1, null, null, (long) 0));
        bookCopies.add(new BookCopy("", "", "", (long) 1, null, null, (long) 0));
        bookCopies.add(new BookCopy("", "", "", (long) 2, null, null, (long) 0));
        bookCopies.add(new BookCopy("", "", "", (long) 2, null, null, (long) 0));
        bookCopies.add(new BookCopy("", "", "", (long) 3, null, null, (long) 0));

        List<NumberToLibrary> numberToLibraries = (List<NumberToLibrary>) getNumberEachLibrary.invoke(searchService, bookCopies, libraries);
        numberToLibraries.forEach(numberToLibrary -> {
            if (numberToLibrary.getLibraryName().equals("1号")) {
                assertEquals(numberToLibrary.getNumber(), 3);
            } else if (numberToLibrary.getLibraryName().equals("2号")) {
                assertEquals(numberToLibrary.getNumber(), 2);
            } else if (numberToLibrary.getLibraryName().equals("3号")) {
                assertEquals(numberToLibrary.getNumber(), 1);
            }
        });

    }


    @Test
    @Transactional
    public void testGetBookTypeAndCopy() throws Exception {
        //先添加一个booktype
        MultipartFile multipartFile = new MockMultipartFile("test", "1.jpg", "content-type", new FileInputStream("/home/haojie/Pictures/1.jpg"));
        UploadNewBookRequest uploadNewBookRequest = new UploadNewBookRequest(
                multipartFile,
                "isbntestttt",
                "nametest",
                "authortest",
                "descriptiontest",
                "2000-10-06"

        );

        uploadService.handleUpload(uploadNewBookRequest);

        //添加副本
        AddBookCopyRequest addBookCopyRequest4 = new AddBookCopyRequest("isbntestttt", "4", "4");
        AddBookCopyRequest addBookCopyRequest3 = new AddBookCopyRequest("isbntestttt", "3", "3");
        AddBookCopyRequest addBookCopyRequest2 = new AddBookCopyRequest("isbntestttt", "2", "2");
        AddBookCopyRequest addBookCopyRequest1 = new AddBookCopyRequest("isbntestttt", "1", "1");


        uploadService.addBookCopy(addBookCopyRequest1);
        uploadService.addBookCopy(addBookCopyRequest2);
        uploadService.addBookCopy(addBookCopyRequest3);
        uploadService.addBookCopy(addBookCopyRequest4);


        GetBookTypeAndCopyResponse getBookTypeAndCopyResponse = searchService.getBookTypeAndCopy("isbntestttt");

        //测试基本信息对不对
        assertEquals(getBookTypeAndCopyResponse.getName(), "nametest");
        assertEquals(getBookTypeAndCopyResponse.getIsbn(), "isbntestttt");
        assertEquals(getBookTypeAndCopyResponse.getDescription(), "descriptiontest");
        assertEquals(getBookTypeAndCopyResponse.getAuthor(), "authortest");

        //测试每个图书馆的数目对不对
        List<NumberToLibrary> numberEachLibrary = getBookTypeAndCopyResponse.getNumberEachLibrary();
        numberEachLibrary.forEach(numberToLibrary -> {
            switch (numberToLibrary.getLibraryName()) {
                case "邯郸校区图书馆":
                    assertEquals(numberToLibrary.getNumber(), 1);
                    break;
                case "枫林校区图书馆":
                    assertEquals(numberToLibrary.getNumber(), 2);
                    break;
                case "张江校区图书馆":
                    assertEquals(numberToLibrary.getNumber(), 3);
                    break;
                case "江湾校区图书馆":
                    assertEquals(numberToLibrary.getNumber(), 4);
                    break;
            }


        });

        //测试副本数目
        List<BookCopy> bookCopies = getBookTypeAndCopyResponse.getBookCopies();
        assertEquals(bookCopies.size(), 1 + 2 + 3 + 4);

        bookCopies.forEach(bookCopy -> {
            System.out.println(bookCopy.toString());
        });

        //测试一个不存在的isbn
        assertThrows(BookTypeNotFoundException.class, () -> {
            searchService.getBookTypeAndCopy("non_existent_isbn");
        });


    }


    @Test
    @Transactional
    public void testGetBookTypeByISBN() throws Exception {
        MultipartFile multipartFile = new MockMultipartFile("test", "1.jpg", "content-type", new FileInputStream("/home/haojie/Pictures/1.jpg"));
        UploadNewBookRequest uploadNewBookRequest = new UploadNewBookRequest(
                multipartFile,
                "isbntestttt",
                "nametest",
                "authortest",
                "descriptiontest",
                "2000-10-06"

        );

        UploadNewBookRequest uploadNewBookRequest2 = new UploadNewBookRequest(
                multipartFile,
                "isbntestttt2",
                "nametest",
                "authortest",
                "descriptiontest",
                "2000-10-06"

        );

        uploadService.handleUpload(uploadNewBookRequest);
        uploadService.handleUpload(uploadNewBookRequest2);

        List<BookType> bookTypeList = searchService.getBookType("isbntestttt", null, null);

        assertEquals(bookTypeList.size(), 1);
        assertEquals(bookTypeList.get(0).getIsbn(), "isbntestttt");
        assertEquals(bookTypeList.get(0).getName(), "nametest");
        assertEquals(bookTypeList.get(0).getDescription(), "descriptiontest");


    }


    @Test
    @Transactional
    public void testGetBookTypeByAuthor() throws Exception {
        MultipartFile multipartFile = new MockMultipartFile("test", "1.jpg", "content-type", new FileInputStream("/home/haojie/Pictures/1.jpg"));
        UploadNewBookRequest uploadNewBookRequest = new UploadNewBookRequest(
                multipartFile,
                "isbntestttt",
                "nametest",
                "author1",
                "descriptiontest",
                "2000-10-06"

        );

        UploadNewBookRequest uploadNewBookRequest2 = new UploadNewBookRequest(
                multipartFile,
                "isbntestttt2",
                "nametest2",
                "author2",
                "descriptiontest",
                "2000-10-06"

        );

        UploadNewBookRequest uploadNewBookRequest3 = new UploadNewBookRequest(
                multipartFile,
                "isbntestttt22",
                "nametest2",
                "author1",
                "descriptiontest",
                "2000-10-06"

        );

        uploadService.handleUpload(uploadNewBookRequest);
        uploadService.handleUpload(uploadNewBookRequest2);
        uploadService.handleUpload(uploadNewBookRequest3);

        List<BookType> bookTypeList = searchService.getBookType(null, "author1", null);

        assertEquals(bookTypeList.size(), 2);


    }


    @Test
    @Transactional
    public void testGetBookTypeByName() throws Exception {
        MultipartFile multipartFile = new MockMultipartFile("test", "1.jpg", "content-type", new FileInputStream("/home/haojie/Pictures/1.jpg"));
        for (char i = 'a'; i <= 'z'; i++) {
            for (char j = i; j <= 'z'; j++) {
                UploadNewBookRequest uploadNewBookRequest = new UploadNewBookRequest(
                        multipartFile,
                        UUID.randomUUID().toString(),
                        i + String.valueOf(j),
                        "authortest",
                        "descriptiontest",
                        "2000-10-06"

                );
                uploadService.handleUpload(uploadNewBookRequest);

            }
        }

        List<BookType> bookTypes = searchService.getBookType(null, null, "a");
        bookTypes.forEach(bookType -> {
            assertTrue(bookType.getName().contains("a"));
        });
        assertEquals(bookTypes.size(), 26);

    }

    @Test
    @Transactional
    public void testGetBookTypeByNameAndAuthor() throws Exception {
        MultipartFile multipartFile = new MockMultipartFile("test", "1.jpg", "content-type", new FileInputStream("/home/haojie/Pictures/1.jpg"));
        for (char i = 'a'; i <= 'z'; i++) {
            for (char j = i; j <= 'z'; j++) {
                UploadNewBookRequest uploadNewBookRequest = new UploadNewBookRequest(
                        multipartFile,
                        UUID.randomUUID().toString(),
                        i + String.valueOf(j),
                        String.valueOf(i),
                        "descriptiontest",
                        "2000-10-06"

                );
                uploadService.handleUpload(uploadNewBookRequest);

            }
        }

        List<BookType> bookTypes = searchService.getBookType(null, "a", "a");
        bookTypes.forEach(bookType -> {
            assertTrue(bookType.getName().contains("a"));
            assertEquals(bookType.getAuthor(), "a");
        });
        assertEquals(bookTypes.size(), 26);

    }



}