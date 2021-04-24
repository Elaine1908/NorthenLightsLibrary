package com.example.lab2.service;

import com.example.lab2.entity.User;
import com.example.lab2.exception.RegisterException;
import com.example.lab2.request.auth.AddAdminRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.omg.CORBA.UserException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.SQLIntegrityConstraintViolationException;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class UserDetailsServiceImplTest {

    @Resource(name = "userService")
    UserDetailsServiceImpl userDetailsService;

    @Test
    @Transactional()
    public void selectUserByName() throws SQLIntegrityConstraintViolationException {

        User user = new User("luanqibazao", "luanqibazao", "email@email.com", User.STUDENT, User.MAX_CREDIT);

        UserDetailsServiceImpl.save(user);

        User selectedUser = userDetailsService.selectUserByName("luanqibazao").get();

        assertEquals(selectedUser.getUsername(), "luanqibazao");
        assertEquals(selectedUser.getPassword(), "luanqibazao");
        assertEquals(selectedUser.getEmail(), "email@email.com");
        assertEquals(selectedUser.getRole(), User.STUDENT);
        assertEquals(selectedUser.getCredit(), User.MAX_CREDIT);

    }

    @Test
    @Transactional
    public void save() throws SQLIntegrityConstraintViolationException {
        User user = new User("luanqibazao", "luanqibazao", "email@email.com", User.STUDENT, User.MAX_CREDIT);

        UserDetailsServiceImpl.save(user);

        User selectedUser = userDetailsService.selectUserByName("luanqibazao").get();

        assertEquals(selectedUser.getUsername(), "luanqibazao");
        assertEquals(selectedUser.getPassword(), "luanqibazao");
        assertEquals(selectedUser.getEmail(), "email@email.com");
        assertEquals(selectedUser.getRole(), User.STUDENT);
        assertEquals(selectedUser.getCredit(), User.MAX_CREDIT);

        //重复添加测试
        User user2 = new User("luanqibazao", "luanqibazao", "email@email.com", User.STUDENT, User.MAX_CREDIT);
        assertThrows(DataIntegrityViolationException.class, () -> {
            UserDetailsServiceImpl.save(user2);
        });

    }

    @Test
    @Transactional
    public void loadUserByUsername() throws SQLIntegrityConstraintViolationException {
        User user = new User("luanqibazao", "luanqibazao", "email@email.com", User.STUDENT, User.MAX_CREDIT);

        UserDetailsServiceImpl.save(user);

        User selectedUser = userDetailsService.selectUserByName("luanqibazao").get();

        assertEquals(selectedUser.getUsername(), "luanqibazao");
        assertEquals(selectedUser.getPassword(), "luanqibazao");
        assertEquals(selectedUser.getEmail(), "email@email.com");
        assertEquals(selectedUser.getRole(), User.STUDENT);
        assertEquals(selectedUser.getCredit(), User.MAX_CREDIT);
    }

    @Test
    @Transactional
    public void addAdmin() {

        AddAdminRequest addAdminRequest = new AddAdminRequest("adminusername", "adminpassword", "email@email.com");

        //正常添加
        userDetailsService.addAdmin(addAdminRequest);

        User user = userDetailsService.selectUserByName("adminusername").get();

        assertEquals(user.getUsername(), "adminusername");
        assertEquals(user.getPassword(), "adminpassword");
        assertEquals(user.getEmail(), "email@email.com");


        //用户名重复添加
        assertThrows(RegisterException.class, () -> {
            userDetailsService.addAdmin(addAdminRequest);
        });


    }
}