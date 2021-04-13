package com.example.lab2.service;

import com.example.lab2.dao.LibraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class CampusService {
    @Autowired
    LibraryRepository campusRepo;
    static LibraryRepository libraryRepository;

    @PostConstruct
    public void init() {
        libraryRepository = campusRepo;
    }

    public static boolean campusExists(Long campusID) {
        return libraryRepository.existsById(campusID);
    }


}
