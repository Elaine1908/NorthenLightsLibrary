package com.example.lab2.dao;


import com.example.lab2.dto.due.DueFineDTO;
import com.example.lab2.entity.Fine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FineRepository extends JpaRepository<Fine, Long> {

    @Query("select f from Fine f where f.userID=:userID")
    public List<Fine> getFineByUserID(@Param("userID") long userID);


    @Query("select new com.example.lab2.dto.due.DueFineDTO(f.fineID,f.money,f.reason,f.date,u.username,u.email) from Fine f left join User u on u.user_id=f.userID")
    public List<DueFineDTO> getAllDueFineDTO();

}
