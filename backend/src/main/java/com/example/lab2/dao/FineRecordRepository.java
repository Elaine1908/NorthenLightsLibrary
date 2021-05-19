package com.example.lab2.dao;

import com.example.lab2.entity.Fine;
import com.example.lab2.entity.FineRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.Optional;

public interface FineRecordRepository extends JpaRepository<FineRecord, Long> {

    @Query("select f from FineRecord f where f.userID=:userID")
    public List<FineRecord> getFineRecordByUserID(@Param("userID") long userID);

    @Query("select fr from FineRecord fr where fr.uuid=:uuid")
    public Optional<FineRecord> getFineRecordByUuid(@Param("uuid") String uuid);
}

