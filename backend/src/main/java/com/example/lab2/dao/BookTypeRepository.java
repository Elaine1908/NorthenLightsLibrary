package com.example.lab2.dao;

import com.example.lab2.dto.BookDTO;
import com.example.lab2.entity.BookType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.swing.text.html.Option;
import java.util.Optional;

/**
 * 从数据库查询书籍信息的dao层
 */
public interface BookTypeRepository extends JpaRepository<BookType, Long> {


    @Query("select b_t from BookType b_t where b_t.name=:bookTypeName")
    public Optional<BookType> getBookTypeByName(@Param("bookTypeName") String bookTypeName);

    @Query("select b_t from BookType b_t where b_t.isbn=:isbn")
    public Optional<BookType> getBookTypeByISBN(@Param("isbn") String isbn);

}
