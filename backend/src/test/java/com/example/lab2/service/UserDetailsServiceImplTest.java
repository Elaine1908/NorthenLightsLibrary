package com.example.lab2.service;

import com.example.lab2.entity.User;
import org.junit.jupiter.api.Test;

import javax.swing.text.html.Option;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserDetailsServiceImplTest {

    @Test
    void selectUserByName() {
    }

    @Test
    void getUser() {
    }

    @Test
    void save() {
    }

    @Test
    void login() {
    }

    @Test
    void loadUserByUsername() {
        UserDetailsServiceImpl userDetailsService = new UserDetailsServiceImpl();
        User user = (User) userDetailsService.loadUserByUsername("haojie");
        assertEquals(user.getUsername(), "haojie");
        assertEquals(user.getEmail(), "zhj630985214@gmail.com");
        assertEquals(user.getPassword(), "000000");
        assertEquals(user.getRole(), "student");
        assertFalse(user.getAuthorities().isEmpty());
    }
}