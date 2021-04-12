package com.example.lab2.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Date;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
public class Book {

    //书籍的四种状态
    public static final String AVAILABLE = "available";
    public static final String BORROWED = "borrowed";
    public static final String RESERVED = "reserved";
    public static final String DAMAGED = "damaged";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long bookID;


    @Column(unique = false)
    private String isbn;
    private String name;
    private String author;
    private String description;
    private Date publicationDate;

    @JsonIgnore
    @Column
    private String imagePath;
    private long campusID;
    private String status;
}
