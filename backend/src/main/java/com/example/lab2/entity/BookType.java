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
public class BookType implements Comparable<BookType> {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long bookID;


    @Column(unique = true)
    private String isbn;

    private String name;
    private String author;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(columnDefinition = "text")
    private String description;
    private Date publicationDate;
    private String imagePathToFrontEnd;

    @JsonIgnore
    @Column
    private String imagePath;


    /**
     * 将hashcode设置成isbn的hashcode
     *
     * @return hashcode
     */
    @Override
    public int hashCode() {
        if (this.isbn != null) {
            return this.isbn.hashCode();
        }
        return super.hashCode();
    }


    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BookType)) {
            return false;
        }
        BookType bookType = (BookType) o;
        return
                this.bookID == ((BookType) o).bookID &&
                        this.name.equals(bookType.name) &&
                        this.author.equals(bookType.author) &&
                        this.description.equals(bookType.getDescription()) &&
                        this.isbn.equals(bookType.isbn) &&
                        this.imagePath.equals(bookType.imagePath);


    }

    @Override
    public int compareTo(BookType bookType) {
        return (int) (this.bookID - bookType.bookID);
    }
}
