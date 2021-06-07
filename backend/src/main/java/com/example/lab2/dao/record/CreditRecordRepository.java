package com.example.lab2.dao.record;


import com.example.lab2.entity.CreditRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditRecordRepository extends JpaRepository<CreditRecord, Long> {

}
