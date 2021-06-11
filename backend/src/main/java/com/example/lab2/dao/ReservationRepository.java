package com.example.lab2.dao;


import com.example.lab2.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("select rv from Reservation rv where rv.bookCopyID=:bookCopyID")
    public Optional<Reservation> getReservationByBookCopyID(@Param("bookCopyID") Long bookCopyID);

    //根据用户id，获得他一共预约了几本书
    @Query("select count(rv.reservationID) from Reservation rv where rv.userID=:userID")
    public Long getReservationCountByUserID(@Param("userID") Long userID);

    public void deleteReservationByBookCopyID(Long bookCopyID);

    @Query("select new java.lang.Long(count(r.reservationID)) from Reservation r left join User u on r.userID=u.user_id where u.username=:username")
    public Long getReservationCountByUsername(@Param("username") String username);

}
