package com.example.lab2.dao.record;


import com.example.lab2.dto.record.CreditRecordDTO;
import com.example.lab2.entity.CreditRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreditRecordRepository extends JpaRepository<CreditRecord, Long> {


    @Query("select new com.example.lab2.dto.record.CreditRecordDTO(cr.amount,cr.description,cr.time)" +
            "from CreditRecord cr left join User u on cr.userID=u.user_id where u.username=:username")
    public List<CreditRecordDTO> getCreditRecordByUsername(@Param("username") String username);

}
