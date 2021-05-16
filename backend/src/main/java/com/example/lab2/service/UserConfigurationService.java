package com.example.lab2.service;

import com.example.lab2.dao.UserConfigurationRepository;
import com.example.lab2.entity.UserConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userConfigurationService")
public class UserConfigurationService {

    @Autowired
    private UserConfigurationRepository userConfigurationRepository;


    public List<UserConfiguration> getAllUserConfiguration() {
        return userConfigurationRepository.findAll();
    }
}
