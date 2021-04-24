package com.example.lab2.dao;

import com.example.lab2.entity.BookType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * 从数据库查询书籍信息的dao层
 */
public interface BookTypeRepository extends JpaRepository<BookType, Long> {


    @Query("select b_t from BookType b_t where b_t.name=:bookTypeName")
    public Optional<BookType> getBookTypeByName(@Param("bookTypeName") String bookTypeName);

    @Query("select b_t from BookType b_t where b_t.isbn=:isbn")
    public Optional<BookType> getBookTypeByISBN(@Param("isbn") String isbn);

    @Query("select b_t from BookType b_t where b_t.isbn=:isbn")
    public List<BookType> getAllBookTypeByISBN(@Param("isbn") String isbn);

    @Query("select b_t from BookType b_t where b_t.author=:author")
    public List<BookType> getAllBookTypeByAuthor(@Param("author") String author);

    @Query("select b_t from BookType b_t where b_t.name LIKE CONCAT('%',?1,'%')")
    public List<BookType> getAllBookTypeByNameFuzzySearch(@Param("bookName") String bookName);

}
