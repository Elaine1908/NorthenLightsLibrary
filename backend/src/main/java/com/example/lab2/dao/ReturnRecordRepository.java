package com.example.lab2.dao;

import com.example.lab2.entity.ReturnRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReturnRecordRepository extends JpaRepository<ReturnRecord,Long> {

    @Query("select f from ReturnRecord f where f.userID=:userID")
    public List<ReturnRecord> getReturnRecordByUserID(@Param("userID") long userID);
}
