package com.example.lab2.dao;

import com.example.lab2.entity.BookCopy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface BookCopyRepository extends JpaRepository<BookCopy, Long> {


    @Query("select new java.lang.Long(count(b_c.bookCopyID)) from BookCopy b_c where b_c.isbn=:isbn")
    public Long getBookCopyCountByISBN(@Param("isbn") String isbn);

    @Query("select new BookCopy(b_c.libraryID,b_c.status,b_c.uniqueBookMark,u.username,library.libraryName,library.libraryID) from BookCopy b_c " +
            "left join Borrow borrow on borrow.uniqueBookMark=b_c.uniqueBookMark " +
            "left join User u on u.user_id=borrow.userID " +
            "left join Library library on library.libraryID=b_c.libraryID " +
            " where b_c.isbn=:isbn")
    public List<BookCopy> getBookCopiesByISBN(@Param("isbn") String isbn);


}
