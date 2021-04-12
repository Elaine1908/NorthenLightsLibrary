package com.example.lab2.service;

import com.example.lab2.dao.CampusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class CampusService {
    @Autowired
    CampusRepository campusRepo;
    static CampusRepository campusRepository;

    @PostConstruct
    public void init() {
        campusRepository = campusRepo;
    }

    public static boolean campusExists(Long campusID) {
        return campusRepository.existsById(campusID);
    }


}
