package com.example.lab2.dao;

import com.example.lab2.entity.UserConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserConfigurationRepository extends JpaRepository<UserConfiguration, Long> {

    @Query("select uc from UserConfiguration uc where uc.role=:role")
    public Optional<UserConfiguration> findUserConfigurationByRole(@Param("role") String role);

}
