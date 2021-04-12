package com.example.lab2.controller;

import com.example.lab2.dao.CampusRepository;
import com.example.lab2.entity.Book;
import com.example.lab2.entity.Campus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@RequestMapping("/initialize")
@Component
public class InitializeCampusController {
    @Autowired
    CampusRepository campusRepository;

    @PostConstruct
    public ResponseEntity<String> hello() {

        Campus c1 = new Campus("邯郸校区");
        Campus c2 = new Campus("枫林校区");
        Campus c3 = new Campus("张江校区");
        Campus c4 = new Campus("江湾校区");
        try {
            campusRepository.save(c1);
            campusRepository.save(c2);
            campusRepository.save(c3);
            campusRepository.save(c4);
        } catch (Exception ignored) {
        }

        return ResponseEntity.ok("hello,fudan!");


    }

}
