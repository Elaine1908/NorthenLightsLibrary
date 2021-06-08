package com.example.lab2.dao.record;

import com.example.lab2.dto.record.ReturnRecordDTO;
import com.example.lab2.entity.ReturnRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReturnRecordRepository extends JpaRepository<ReturnRecord, Long> {

    @Query("select new com.example.lab2.dto.record.ReturnRecordDTO(b.time,b.uniqueBookMark,u.username,b.admin,l.libraryName) from ReturnRecord b left join User u on u.user_id=b.userID " +
            "left join Library l on l.libraryID=b.libraryID" +
            " where u.username=:username")
    public List<ReturnRecordDTO> getReturnRecordByUsername(@Param("username") String username);

    @Query("select new com.example.lab2.dto.record.ReturnRecordDTO(r.time,r.uniqueBookMark,u.username,r.admin,l.libraryName) from ReturnRecord r left join User u on u.user_id=r.userID left join Library l on l.libraryID=r.libraryID where r.uniqueBookMark=:uniqueBookMark")
    public List<ReturnRecordDTO> getBookCopyReturnRecordByUniqueBookMark(@Param("uniqueBookMark") String uniqueBookMark);

    @Query("select r from ReturnRecord r where r.userID=:userID and r.uniqueBookMark LIKE CONCAT(?2,'%')")
    public List<ReturnRecord> getReturnRecordByUserIDAndISBN(@Param("userID") long userID,@Param("isbn")String isbn);
}
