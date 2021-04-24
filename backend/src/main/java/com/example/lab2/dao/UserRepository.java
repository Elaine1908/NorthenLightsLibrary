package com.example.lab2.dao;

import com.example.lab2.dto.UserDTO;
import com.example.lab2.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

//从数据库查询用户信息的接口层
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u from User u where u.username = :username")
    Optional<User> findByName(@Param("username") String name);

    @Modifying
    @Query("update User u set u.password = ?1 where u.username = ?2")
    void updatePasswordByUsername(String password, String username);

    @Query("select u.credit from User u where u.user_id = :id")
    int findCreditById(@Param("id") Long id);

    User getUserByUsernameAndPassword(String username, String password);

    @Query("select u from User u where u.username=:username")
    User getUserByUsername(@Param("username") String username);

    @Query("select new com.example.lab2.dto.UserDTO(u.username,u.email) from User u where u.role='superadmin' OR u.role='admin'")
    List<UserDTO> getAllAdmin();
}

