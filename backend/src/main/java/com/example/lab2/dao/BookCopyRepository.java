package com.example.lab2.dao;

import com.example.lab2.dto.BookCopyDTO;
import com.example.lab2.dto.BorrowedBookCopyDTO;
import com.example.lab2.dto.ReservedBookCopyDTO;
import com.example.lab2.dto.ShowBookCopyDTO;
import com.example.lab2.entity.BookCopy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface BookCopyRepository extends JpaRepository<BookCopy, Long> {


    @Query("select new java.lang.Long(count(b_c.bookCopyID)) from BookCopy b_c where b_c.isbn=:isbn")
    public Long getBookCopyCountByISBN(@Param("isbn") String isbn);

    @Query("select new com.example.lab2.dto.BookCopyDTO(library.libraryID,library.libraryName,b_c.status,b_c.uniqueBookMark,u.username) from BookCopy b_c " +
            "left join Borrow borrow on borrow.uniqueBookMark=b_c.uniqueBookMark " +
            "left join User u on u.user_id=borrow.userID " +
            "left join Library library on library.libraryID=b_c.libraryID " +
            " where b_c.isbn=:isbn")
    public List<BookCopyDTO> getBookCopiesByISBN(@Param("isbn") String isbn);//显示一本书及其副本信息

    @Query("select new com.example.lab2.dto.BorrowedBookCopyDTO(borrow.borrowDate,b_t.isbn,b_t.author,b_t.name,b_c.uniqueBookMark,b_t.imagePathToFrontEnd)" +
            "from Borrow borrow left join BookCopy b_c on borrow.uniqueBookMark=b_c.uniqueBookMark left join BookType b_t on b_t.isbn=b_c.isbn left join User  u on u.user_id=borrow.userID where u.username=:username")
    public List<BorrowedBookCopyDTO> getBorrowedBookCopiesByUsername(@Param("username") String username);//显示用户借阅的书本信息

    @Query("select b_c from BookCopy b_c where b_c.uniqueBookMark=:uniqueBookMark")
    public Optional<BookCopy> getBookCopyByUniqueBookMark(@Param("uniqueBookMark") String uniqueBookMark);

    @Query("select new com.example.lab2.dto.ReservedBookCopyDTO(rv.reservationDate,bt.isbn,bt.author,bt.name,bc.uniqueBookMark,l.libraryName,bt.imagePathToFrontEnd) " +
            "from Reservation rv left join BookCopy bc on rv.bookCopyID=bc.bookCopyID left join BookType bt on bt.isbn=bc.isbn left join User u on u.user_id=rv.userID left join Library l on l.libraryID=bc.libraryID where u.username=:username")
    public List<ReservedBookCopyDTO> getAllReservedBooksByUsername(@Param("username") String username);

    @Query("select new com.example.lab2.dto.ShowBookCopyDTO(b_c.isbn,bt.author,bt.name,b_c.uniqueBookMark,l.libraryName,bt.imagePathToFrontEnd) from BookCopy b_c left join BookType bt on bt.isbn=b_c.isbn left join Library l on l.libraryID=b_c.libraryID where b_c.uniqueBookMark=:uniqueBookMark")
    public Optional<ShowBookCopyDTO> getBookCopyByUniqueBookMarkAndShow(@Param("uniqueBookMark") String uniqueBookMark);


}
