package com.example.lab2.utils;

import com.example.lab2.entity.User;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilsTest {

    @Test
    void generateJwt() {
        User user = new User();
        user.setUsername("haojie");
        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("student"));
        user.setAuthorities(authorities);
        String jwt = JwtUtils.generateJwt(user);
        System.out.println(jwt);
        System.out.println(JwtUtils.getUserName(jwt));


    }

    @Test
    void getUserName() {


    }

    @Test
    void getRole() {
    }
}