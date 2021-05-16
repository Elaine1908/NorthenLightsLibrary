package com.example.lab2.dao;

import com.example.lab2.entity.BookType;
import com.example.lab2.request.upload.UploadNewBookRequest;
import com.example.lab2.service.UploadService;
import net.bytebuddy.agent.builder.AgentBuilder;
import org.junit.Assert;
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
import java.util.List;

import static org.junit.Assert.*;


@SpringBootTest
@RunWith(SpringRunner.class)
public class BookTypeRepositoryTest {

    @Autowired
    BookTypeRepository bookTypeRepository;

    @Resource(name = "uploadService")
    UploadService uploadService;


    @Test
    @Transactional
    public void TestGetAllBookTypeByNameFuzzySearch() throws Exception {
        MultipartFile multipartFile = new MockMultipartFile("test", "1.jpg", "content-type", new FileInputStream("/home/haojie/Pictures/1.jpg"));
        UploadNewBookRequest uploadNewBookRequest = new UploadNewBookRequest(
                multipartFile,
                "23423525",
                "张三",
                "authortest",
                "descriptiontest",
                "2000-10-06",
                1000

        );

        UploadNewBookRequest uploadNewBookRequest2 = new UploadNewBookRequest(
                multipartFile,
                "34234234",
                "三脚猫",
                "authortest",
                "descriptiontest",
                "2000-10-06",
                1000

        );

        UploadNewBookRequest uploadNewBookRequest3 = new UploadNewBookRequest(
                multipartFile,
                "2fgdfg",
                "李四",
                "authortest",
                "descriptiontest",
                "2000-10-06",
                1000

        );

        uploadService.handleUpload(uploadNewBookRequest);
        uploadService.handleUpload(uploadNewBookRequest2);
        uploadService.handleUpload(uploadNewBookRequest3);

        List<BookType> bookTypes = bookTypeRepository.getAllBookTypeByNameFuzzySearch("三");

        assertEquals(bookTypes.size(), 2);

    }


}