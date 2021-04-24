package com.example.lab2.service;
import com.example.lab2.dao.BookCopyRepository;
import com.example.lab2.dao.BookTypeRepository;
import com.example.lab2.dao.LibraryRepository;
import com.example.lab2.entity.BookCopy;
import com.example.lab2.entity.BookType;
import com.example.lab2.entity.Library;
import com.example.lab2.exception.UploadException;
import com.example.lab2.request.upload.AddBookCopyRequest;
import com.example.lab2.response.GeneralResponse;
import com.example.lab2.request.upload.UploadNewBookRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service("uploadService")
public class UploadService {

    @Autowired
    BookTypeRepository bookTypeRepository;

    @Autowired
    LibraryRepository libraryRepository;

    @Autowired
    BookCopyRepository bookCopyRepository;


    @Value("${images.whereisbookcovers}")
    String whereis;

    @Value("${images.whereisbookcoversToFrontEnd}")
    String whereIsForFrontEnd;


    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public GeneralResponse handleUpload(UploadNewBookRequest uploadNewBookRequest) {

        //生成随机文件名
        String fileName = UUID.randomUUID().toString();
        String originalFileName = uploadNewBookRequest.getBookcoverimage().getOriginalFilename();

        if (originalFileName == null) {
            throw new UploadException("上传失败");
        }

        //获得源文件扩展名
        String extensionName = originalFileName.split("\\.")[originalFileName.split("\\.").length - 1];

        //得到book对象
        BookType bookType = uploadNewBookRequest.getBook();
        bookType.setImagePath(whereis + "/" + fileName + "." + extensionName);
        bookType.setImagePathToFrontEnd(whereIsForFrontEnd + "/" + fileName + "." + extensionName);

        //检查是否有重复isbn的
        Optional<BookType> hasRepeatedISBN = bookTypeRepository.getBookTypeByISBN(uploadNewBookRequest.getIsbn());
        if (hasRepeatedISBN.isPresent()) {
            throw new UploadException("根据isbn来看，这种书已经存在了");
        }


        //尝试更新数据库及写入文件
        try {
            this.updateDatabaseAndSaveFile(
                    uploadNewBookRequest.getBookcoverimage(), bookType
            );
        } catch (IOException e) {
            throw new UploadException("上传失败");
        }


        return new GeneralResponse("上传成功");


    }

    /**
     * Service层，添加书的副本的处理函数
     *
     * @param addBookCopyRequest
     * @return
     */
    public GeneralResponse addBookCopy(AddBookCopyRequest addBookCopyRequest) {
        String isbn = addBookCopyRequest.getIsbn();
        Long number = Long.parseLong(addBookCopyRequest.getNumber());
        Long libraryID = Long.parseLong(addBookCopyRequest.getLibraryID());

        //检测是否能找到这个图书馆
        Optional<Library> libraryOptional = libraryRepository.findById(libraryID);
        if (!libraryOptional.isPresent()) {
            throw new UploadException("找不到这个图书馆，请重试");
        }

        //设置一次性上传的副本数不能小于0,也不能大于10
        if (number <= 0 || number > 10) {
            throw new UploadException("添加的副本数目必须在1～10本之间！");
        }


        //检测是否能找到这一种书
        Optional<BookType> bookTypeOptional = bookTypeRepository.getBookTypeByISBN(isbn);
        if (!bookTypeOptional.isPresent()) {
            throw new UploadException("找不到这本书");
        }

        //因为考虑到要根据从已经有了几本这种书的副本生成uniqueBookMark，因此如果并发调用可能出现问题
        //故在这里加锁
        synchronized (UploadService.class) {

            //仓库里已经有了几本这本书的副本？
            long alreadyHaveNumber = bookCopyRepository.getBookCopyCountByISBN(isbn);

            //bookcopy的编号从第几号开始编？
            long start = alreadyHaveNumber + 1;

            //遍历生成书的副本，加入到数据库里面去
            for (long i = start; i < start + number; i++) {
                String uniqueBookMark = isbn + "-" + i;
                BookCopy newBookCopy = new BookCopy(
                        BookCopy.AVAILABLE,
                        isbn,
                        uniqueBookMark,
                        libraryID,
                        null,
                        null,
                        (long) 0
                );
                bookCopyRepository.save(newBookCopy);

            }

        }

        return new GeneralResponse(
                String.format("上传副本成功！共上传了%d本副本！", number)
        );


    }


    /**
     * 将book信息更新数据库，并将multipartFile的文件写入。如果写入失败会回滚
     *
     * @param multipartFile 文件
     * @param bookType      书本信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateDatabaseAndSaveFile(MultipartFile multipartFile, BookType bookType) throws IOException {
        bookTypeRepository.save(bookType);
        multipartFile.transferTo(new File(bookType.getImagePath()));

    }


}
