package com.example.lab2.dao;

import com.example.lab2.dto.ReserveRecordDTO;
import com.example.lab2.entity.ReserveRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReserveRecordRepository extends JpaRepository<ReserveRecord,Long> {

    @Query("select new com.example.lab2.dto.ReserveRecordDTO(r.time,r.uniqueBookMark,u.username) from ReserveRecord r left join User u on " +
            "u.user_id=r.userID where u.username=:username")
    public List<ReserveRecordDTO> getReserveRecordByUsername(@Param("username") String username);
}