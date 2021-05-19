package com.example.lab2.dao;

import com.example.lab2.dto.BorrowRecordDTO;
import com.example.lab2.entity.BorrowRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord,Long> {

    @Query("select new com.example.lab2.dto.BorrowRecordDTO(b.time,b.uniqueBookMark,u.username,b.admin,l.libraryName) from BorrowRecord b left join User u on u.user_id=b.userID " +
            "left join Library l on l.libraryID=b.libraryID"+
            " where u.username=:username")
    public List<BorrowRecordDTO> getBorrowRecordByUsername(@Param("username") String username);
}
