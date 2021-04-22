package com.example.lab2.service;

import com.example.lab2.dao.BookkCopyRepository;
import com.example.lab2.dao.BookTypeRepository;
import com.example.lab2.entity.BookType;
import com.example.lab2.exception.UploadException;
import com.example.lab2.request.upload.AddBookCopyRequest;
import com.example.lab2.request.upload.UploadNewBookRequest;
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
import java.io.IOException;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UploadServiceTest {


    @Autowired
    BookTypeRepository bookTypeRepository;

    @Autowired
    BookkCopyRepository bookCopyRepository;


    @Resource(name = "uploadService")
    UploadService uploadService;

    /**
     * 测试上传
     *
     * @throws IOException
     */
    @Test
    @Transactional
    public void testHandleUpload() throws IOException {

        //测试正常使用
        MultipartFile multipartFile = new MockMultipartFile("test", "1.jpg", "content-type", new FileInputStream("/home/haojie/Pictures/1.jpg"));
        UploadNewBookRequest uploadNewBookRequest = new UploadNewBookRequest(
                multipartFile,
                "isbntest",
                "nametest",
                "authortest",
                "descriptiontest",
                "2000-10-06"

        );

        uploadService.handleUpload(uploadNewBookRequest);

        BookType selectedBookType = bookTypeRepository.getBookTypeByName("nametest").get();
        assertEquals(selectedBookType.getName(), "nametest");
        assertEquals(selectedBookType.getIsbn(), "isbntest");
        assertEquals(selectedBookType.getAuthor(), "authortest");
        assertEquals(selectedBookType.getDescription(), "descriptiontest");


    }

    /**
     * 测试重复的isbn上传
     */
    @Test
    @Transactional
    public void testHandleRepeatedUpload() throws IOException {

        //测试正常使用
        MultipartFile multipartFile = new MockMultipartFile("test", "1.jpg", "content-type", new FileInputStream("/home/haojie/Pictures/1.jpg"));
        UploadNewBookRequest uploadNewBookRequest = new UploadNewBookRequest(
                multipartFile,
                "isbntest",
                "nametest",
                "authortest",
                "descriptiontest",
                "2000-10-06"

        );

        uploadService.handleUpload(uploadNewBookRequest);

        BookType selectedBookType = bookTypeRepository.getBookTypeByName("nametest").get();
        assertEquals(selectedBookType.getName(), "nametest");
        assertEquals(selectedBookType.getIsbn(), "isbntest");
        assertEquals(selectedBookType.getAuthor(), "authortest");
        assertEquals(selectedBookType.getDescription(), "descriptiontest");

        assertThrows(UploadException.class, () -> {
            uploadService.handleUpload(uploadNewBookRequest);
        });

    }


    /**
     * 测试正常的添加副本
     *
     * @throws IOException
     */
    @Test
    @Transactional
    public void testNormalAddCopy() throws IOException {

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

        long originalCnt = bookCopyRepository.getBookCopyCountByISBN("isbntestttt");


        //尝试添加副本
        AddBookCopyRequest addBookCopyRequest = new AddBookCopyRequest("isbntestttt", "4", "5");
        uploadService.addBookCopy(addBookCopyRequest);

        long nowCnt = bookCopyRepository.getBookCopyCountByISBN("isbntestttt");

        assertEquals(originalCnt + 5, nowCnt);


    }

    /**
     * 测试isbn不存在的情况
     */
    @Test
    @Transactional
    public void testAddCopyISBNNonExist() {
        AddBookCopyRequest addBookCopyRequest = new AddBookCopyRequest("non_exist_isbn", "4", "5");
        assertThrows(UploadException.class, () -> {
            uploadService.addBookCopy(addBookCopyRequest);

        });


    }

    /**
     * 测试图书馆不存在的情况
     *
     * @throws IOException
     */
    @Test
    @Transactional
    public void testAddCopyLibraryNonExist() throws IOException {

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

        AddBookCopyRequest addBookCopyRequest = new AddBookCopyRequest("isbntestttt", "9999999", "5");
        assertThrows(UploadException.class, () -> {
            uploadService.addBookCopy(addBookCopyRequest);

        });
    }


    /**
     * 测试添加书本的拷贝为负数的情况
     */
    @Test
    @Transactional
    public void testAddCopyNegativeCnt() throws IOException {
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

        AddBookCopyRequest addBookCopyRequest = new AddBookCopyRequest("isbntestttt", "1", "-5");
        assertThrows(UploadException.class, () -> {
            uploadService.addBookCopy(addBookCopyRequest);

        });


    }


}