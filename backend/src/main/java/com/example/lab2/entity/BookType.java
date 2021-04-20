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
public class BookType {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long bookID;


    @Column(unique = true)
    private String isbn;

    private String name;
    private String author;
    private String description;
    private Date publicationDate;
    private String imagePathToFrontEnd;

    @JsonIgnore
    @Column
    private String imagePath;
}
