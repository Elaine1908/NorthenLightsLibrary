package com.example.lab2.service;

import com.example.lab2.dao.BookRepository;
import com.example.lab2.entity.Book;
import com.example.lab2.exception.UploadException;
import com.example.lab2.response.GeneralResponse;
import com.example.lab2.request.upload.UploadNewBookRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
public class UploadService {

    @Autowired
    BookRepository bookRepo;
    static BookRepository bookRepository;


    @Value("${images.whereisbookcovers}")
    String whereis;
    static String whereisbookcovers;

    static UploadService uploadService;


    public static GeneralResponse handleUpload(UploadNewBookRequest uploadNewBookRequest) {
        //检验校园是否存在
        if (!CampusService.campusExists(uploadNewBookRequest.getCampusID())) {
            throw new UploadException("找不到这个校区！");
        }

        //生成随机文件名
        String fileName = UUID.randomUUID().toString();

        //得到book对象
        Book book = uploadNewBookRequest.getBook();
        book.setImagePath(whereisbookcovers + "/" + fileName);

        //尝试更新数据库及写入文件
        try {
            uploadService.updateDatabaseAndSaveFile(
                    uploadNewBookRequest.getBookcoverimage(), book
            );
        } catch (IOException e) {
            throw new UploadException("上传失败");
        }

        return new GeneralResponse("上传成功");
    }


    /**
     * 将book信息更新数据库，并将multipartFile的文件写入。如果写入失败会回滚
     *
     * @param multipartFile 文件
     * @param book          书本信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateDatabaseAndSaveFile(MultipartFile multipartFile, Book book) throws IOException {
        bookRepository.save(book);
        multipartFile.transferTo(new File(book.getImagePath()));

    }


    @PostConstruct
    public void init() {
        bookRepository = bookRepo;
        whereisbookcovers = whereis;
        uploadService = new UploadService();

    }
}
