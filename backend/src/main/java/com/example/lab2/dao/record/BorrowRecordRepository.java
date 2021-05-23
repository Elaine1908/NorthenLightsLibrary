package com.example.lab2.dao.record;

import com.example.lab2.dto.record.BorrowRecordDTO;
import com.example.lab2.entity.BorrowRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord,Long> {

    @Query("select new com.example.lab2.dto.record.BorrowRecordDTO(b.time,b.uniqueBookMark,u.username,b.admin,l.libraryName) from BorrowRecord b left join User u on u.user_id=b.userID " +
            "left join Library l on l.libraryID=b.libraryID"+
            " where u.username=:username")
    public List<BorrowRecordDTO> getBorrowRecordByUsername(@Param("username") String username);

    @Query("select new com.example.lab2.dto.record.BorrowRecordDTO(r.time,r.uniqueBookMark,u.username,r.admin,l.libraryName) from BorrowRecord r left join User u on u.user_id=r.userID left join Library l on l.libraryID=r.libraryID where r.uniqueBookMark=:uniqueBookMark")
    public List<BorrowRecordDTO> getBookCopyBorrowRecordByUniqueBookMark(@Param("uniqueBookMark") String uniqueBookMark);




}
