package com.example.lab2.dao;

import com.example.lab2.entity.Borrow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.lang.Long;

@Repository
public interface BorrowRepository extends JpaRepository<Borrow, Long> {

    @Query("select br from Borrow br where br.uniqueBookMark=:uniqueBookMark")
    public Optional<Borrow> getBorrowByUniqueBookMark(@Param("uniqueBookMark") String uniqueBookMark);

    @Query("select new java.lang.Long(count(br.borrowID)) from Borrow br left join User u on br.userID=u.user_id where u.username=:username")
    public Long getBorrowCountByUsername(@Param("username") String username);


}
