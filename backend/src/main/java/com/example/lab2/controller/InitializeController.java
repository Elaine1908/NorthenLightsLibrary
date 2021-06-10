package com.example.lab2.controller;

import com.example.lab2.dao.LibraryRepository;
import com.example.lab2.dao.UserConfigurationRepository;
import com.example.lab2.dao.UserRepository;
import com.example.lab2.entity.Library;
import com.example.lab2.entity.User;
import com.example.lab2.entity.UserConfiguration;
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

    @Autowired
    UserConfigurationRepository userConfigurationRepository;

    /**
     * @return
     * @author haojie
     */
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

        /*各种角色的借阅时长，预约时长，最多借阅多少本书的设置。默认统一最多借阅10本书，预约时长30天，借阅时长30天*/

        //教师的最多借阅多少书，借阅时长，预约时长的设置
        UserConfiguration teacherConfiguration = new UserConfiguration(
                User.TEACHER, 10, 2592000, 2592000
        );
        UserConfiguration undergraduateConfiguration = new UserConfiguration(
                User.UNDERGRADUATE, 10, 2592000, 2592000
        );
        UserConfiguration postgraduateConfiguration = new UserConfiguration(
                User.POSTGRADUATE, 10, 2592000, 2592000
        );


        //将数据存储到数据库中
        try {
            userConfigurationRepository.save(teacherConfiguration);
        } catch (Exception ignored) {
        }
        try {
            userConfigurationRepository.save(undergraduateConfiguration);
        } catch (Exception ignored) {
        }
        try {
            userConfigurationRepository.save(postgraduateConfiguration);
        } catch (Exception ignored) {
        }

        return null;

    }


}
