package com.example.lab2.dao;

import com.example.lab2.dto.BookDTO;
import com.example.lab2.entity.BookCopy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface BookCopyRepository extends JpaRepository<BookCopy, Long> {


    @Query("select new java.lang.Long(count(b_c.bookCopyID)) from BookCopy b_c where b_c.isbn=:isbn")
    public Long getBookCopyCountByISBN(@Param("isbn") String isbn);

    @Query("select new BookCopy(b_c.libraryID,b_c.status,b_c.uniqueBookMark,u.username,library.libraryName,library.libraryID) from BookCopy b_c " +
            "left join Borrow borrow on borrow.uniqueBookMark=b_c.uniqueBookMark " +
            "left join User u on u.user_id=borrow.userID " +
            "left join Library library on library.libraryID=b_c.libraryID " +
            " where b_c.isbn=:isbn")
    public List<BookCopy> getBookCopiesByISBN(@Param("isbn") String isbn);//显示一本书及其副本信息

    @Query("select new com.example.lab2.dto.BookDTO(b_c.isbn,booktype.name,booktype.author,booktype.description,b_c.lastRentDate,b_c.lastReturnDate,booktype.imagePathToFrontEnd,b_c.status,library.libraryName) from BookCopy b_c " +
            "left join Borrow borrow on borrow.uniqueBookMark=b_c.uniqueBookMark " +
            "left join BookType booktype on booktype.isbn=b_c.isbn " +
            "left join Library library on library.libraryID=b_c.libraryID " +
            "left join User u on u.user_id=borrow.userID where u.username=:username")
    public List<BookDTO> getBorrowedBookCopiesByUsername(@Param("username") String username);//显示用户借阅的书本信息

    @Query("select new com.example.lab2.dto.BookDTO(b_c.isbn,booktype.name,booktype.author,booktype.description,b_c.lastReservationDate,booktype.imagePathToFrontEnd,b_c.status,library.libraryName) from BookCopy b_c " +
            "left join Reservation reservation on reservation.bookCopyID=b_c.bookCopyID " +
            "left join BookType booktype on booktype.isbn=b_c.isbn " +
            "left join Library library on library.libraryID=b_c.libraryID " +
            "left join User u on u.user_id=reservation.userID where u.username=:username")
    public List<BookDTO> getReservedBookCopiesByUsername(@Param("username") String username);//显示用户预约的书本信息


    @Query("select b_c from BookCopy b_c where b_c.uniqueBookMark=:uniqueBookMark")
    public Optional<BookCopy> getBookCopyByUniqueBookMark(@Param("uniqueBookMark") String uniqueBookMark);

}
