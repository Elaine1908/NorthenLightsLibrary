package com.example.lab2.service;

import com.example.lab2.dao.BookTypeRepository;
import com.example.lab2.entity.BookType;
import com.example.lab2.exception.UploadException;
import com.example.lab2.request.upload.UploadNewBookRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UploadServiceTest {


    @Autowired
    BookTypeRepository bookTypeRepository;

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

        UploadService.handleUpload(uploadNewBookRequest);

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

        UploadService.handleUpload(uploadNewBookRequest);

        BookType selectedBookType = bookTypeRepository.getBookTypeByName("nametest").get();
        assertEquals(selectedBookType.getName(), "nametest");
        assertEquals(selectedBookType.getIsbn(), "isbntest");
        assertEquals(selectedBookType.getAuthor(), "authortest");
        assertEquals(selectedBookType.getDescription(), "descriptiontest");

        assertThrows(UploadException.class, () -> {
            UploadService.handleUpload(uploadNewBookRequest);
        });

    }

}