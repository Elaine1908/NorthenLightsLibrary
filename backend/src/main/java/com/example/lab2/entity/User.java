package com.example.lab2.entity;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
public class User {

    public static final String ADMIN = "admin";
    public static final String TEACHER = "teacher";
    public static final String STUDENT = "student";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long user_id;//用户id，数据库中的主键

    @Column(unique = true)
    private String username;

    @Column(unique = false)
    private String password;

    @Column
    @Email
    private String email;

    @Column
    private String role;//角色

    @Column
    private int credit;//信用分

    public User(String username, String password, @Email String email, String role,int credit) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.credit = credit;
    }
}
