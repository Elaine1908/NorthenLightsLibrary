package com.example.lab2.dao;

import com.example.lab2.dto.BookDTO;
import com.example.lab2.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * 从数据库查询书籍信息的dao层
 */
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query(
            value = "select new com.example.lab2.dto.BookDTO" +
                    "(b.bookID,b.isbn,b.name,b.author,b.description,b.publicationDate,b.imagePath,b.campusID,b.status,c.campusName)" +
                    "from Book b left join Campus c on b.campusID = c.campusID",
            countQuery = "select count(b.bookID)" +
                    "from Book b left join Campus c on b.campusID = c.campusID"
    )
    public Page<BookDTO> getAllBook(Pageable pageable);



}
