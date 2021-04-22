package com.example.lab2.controller;

import com.example.lab2.dao.LibraryRepository;
import com.example.lab2.dao.UserRepository;
import com.example.lab2.entity.Library;
import com.example.lab2.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

/**
 * 这是管理员的controller
 */
@RestController
@Component
public class InitializeController {
    @Autowired
    LibraryRepository libraryRepository;
    @Autowired
    UserRepository userRepository;

    @PostConstruct
    public ResponseEntity<String> hello() {

        Library c1 = new Library("邯郸校区图书馆");
        Library c2 = new Library("枫林校区图书馆");
        Library c3 = new Library("张江校区图书馆");
        Library c4 = new Library("江湾校区图书馆");
        try {
            libraryRepository.save(c1);
            libraryRepository.save(c2);
            libraryRepository.save(c3);
            libraryRepository.save(c4);
        } catch (Exception ignored) {
        }


        User admin = new User(
                "admin",
                "admin",
                "admin@admin.com",
                User.SUPERADMIN,
                100);
        try {
            userRepository.save(admin);
        } catch (Exception ignored) {
        }

        return null;

    }


}
