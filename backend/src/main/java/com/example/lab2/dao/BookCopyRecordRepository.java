package com.example.lab2.dao;

import com.example.lab2.dto.BookCopyRecordDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookCopyRecordRepository extends JpaRepository<Object,Long> {

    @Query("select new com.example.lab2.dto.BookCopyRecordDTO(r.time,r.uniqueBookMark,u.username,r.admin,l.libraryName,com.example.lab2.dto.BookCopyRecordDTO.reserveRecord) from ReserveRecord r left join User u on u.user_id=r.userID left join Library l on l.libraryID=r.libraryID where r.uniqueBookMark=:isbn")
    public List<BookCopyRecordDTO> getBookCopyReserveRecordByUniqueBookMark(@Param("isbn") String isbn);

    @Query("select new com.example.lab2.dto.BookCopyRecordDTO(r.time,r.uniqueBookMark,u.username,r.admin,l.libraryName,com.example.lab2.dto.BookCopyRecordDTO.borrowRecord) from BorrowRecord r left join User u on u.user_id=r.userID left join Library l on l.libraryID=r.libraryID where r.uniqueBookMark=:isbn")
    public List<BookCopyRecordDTO> getBookCopyBorrowRecordByUniqueBookMark(@Param("isbn") String isbn);

    @Query("select new com.example.lab2.dto.BookCopyRecordDTO(r.time,r.uniqueBookMark,u.username,r.admin,l.libraryName,com.example.lab2.dto.BookCopyRecordDTO.returnRecord) from ReturnRecord r left join User u on u.user_id=r.userID left join Library l on l.libraryID=r.libraryID where r.uniqueBookMark=:isbn")
    public List<BookCopyRecordDTO> getBookCopyReturnRecordByUniqueBookMark(@Param("isbn") String isbn);

}
