package com.example.lab2.dao;

import com.example.lab2.dto.ReturnRecordDTO;
import com.example.lab2.entity.ReturnRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReturnRecordRepository extends JpaRepository<ReturnRecord,Long> {

    @Query("select new com.example.lab2.dto.ReturnRecordDTO(b.time,b.uniqueBookMark,u.username,b.admin,l.libraryName) from ReturnRecord b left join User u on u.user_id=b.userID " +
            "left join Library l on l.libraryID=b.libraryID"+
            " where u.username=:username")
    public List<ReturnRecordDTO> getReturnRecordByUsername(@Param("username") String username);
}
