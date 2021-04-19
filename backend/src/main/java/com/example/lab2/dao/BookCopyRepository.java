package com.example.lab2.dao;

import com.example.lab2.entity.BookCopy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface BookCopyRepository extends JpaRepository<BookCopy, Long> {


    @Query("select new java.lang.Long(count(b_c.bookCopyID)) from BookCopy b_c where b_c.isbn=:isbn")
    public Long getBookCopyCountByISBN(@Param("isbn") String isbn);


}
