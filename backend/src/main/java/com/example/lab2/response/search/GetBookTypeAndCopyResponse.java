package com.example.lab2.response.search;

import com.example.lab2.dto.BookCopyDTO;
import com.example.lab2.entity.BookCopy;
import com.example.lab2.entity.BookType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * 管理员查看书本，显示书本的信息，每个分馆有几本，每个副本的信息
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetBookTypeAndCopyResponse {

    private String author;
    private String imagePathToFrontEnd;
    private String description;
    private String isbn;
    private String name;
    private Date publicationDate;

    private List<NumberToLibrary> numberEachLibrary;

    private List<BookCopyDTO> bookCopies;


    /**
     * 根据booktype创建response
     *
     * @param bookType booktype对象
     */
    public GetBookTypeAndCopyResponse(BookType bookType) {
        this.author = bookType.getAuthor();
        this.imagePathToFrontEnd = bookType.getImagePathToFrontEnd();
        this.description = bookType.getDescription();
        this.isbn = bookType.getIsbn();
        this.name = bookType.getName();
        this.publicationDate = bookType.getPublicationDate();

    }

}


