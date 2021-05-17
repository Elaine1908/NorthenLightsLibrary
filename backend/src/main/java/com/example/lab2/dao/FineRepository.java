package com.example.lab2.dao;


import com.example.lab2.entity.Fine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FineRepository extends JpaRepository<Fine, Long> {

    @Query("select f from Fine f where f.userID=:userID")
    public List<Fine> getFineByUserID(@Param("userID") long userID);

}
