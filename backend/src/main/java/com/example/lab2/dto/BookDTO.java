package com.example.lab2.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

@Data
@Getter
@Setter
public class BookDTO {

    private long bookID;


    private String isbn;
    private String name;
    private String author;
    private String description;
    private Date publicationDate;

    private String imagePath;
    private long campusID;
    private String status;


    //book中没有的属性
    private String campusName;
    private String contentType;
    private byte[] content;


    /**
     * 在将book数据传给前端之前调用这个函数。作用是初始化byte数组content，并返回到json中去
     * 前端与content-type拼接，即可显示图片
     */
    public void setContent() {
        File file = new File(this.imagePath);
        this.content = new byte[(int) file.length()];
        try (FileInputStream in = new FileInputStream(file)) {
            //设置content
            int bytesRead = in.read(this.content);
            if (bytesRead == -1) {
                throw new IOException();
            }

            //设置contentType
            this.contentType = Files.probeContentType(Paths.get(this.imagePath));

        } catch (IOException e) {
            this.content = null;
        }

    }

    public BookDTO(long bookID, String isbn, String name, String author, String description, Date publicationDate, String imagePath, long campusID, String status, String campusName) {
        this.bookID = bookID;
        this.isbn = isbn;
        this.name = name;
        this.author = author;
        this.description = description;
        this.publicationDate = publicationDate;
        this.imagePath = imagePath;
        this.campusID = campusID;
        this.status = status;
        this.campusName = campusName;
    }
}
