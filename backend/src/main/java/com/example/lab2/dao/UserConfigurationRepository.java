package com.example.lab2.dao;

import com.example.lab2.entity.UserConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserConfigurationRepository extends JpaRepository<UserConfiguration, Long> {

}
