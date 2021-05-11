package com.example.lab2.entity;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
public class User implements UserDetails {

    public static final String SUPERADMIN = "superadmin";
    public static final String ADMIN = "admin";
    public static final String TEACHER = "teacher";
    public static final String POSTGRADUATE = "postgraduate";
    public static final String UNDERGRADUATE = "undergraduate";
    public static final String STUDENT = "student";

    public static final int MAX_CREDIT = 100;

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

    @Transient
    private List<? extends GrantedAuthority> authorities;

    @Transient
    private Long libraryID;//管理员用。判断他这次在哪里上班

    public User(String username, String password, @Email String email, String role, int credit) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.credit = credit;
    }

    /**
     * 获得用户的权限列表
     *
     * @return 用户的权限列表
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    /**
     * 帐号是否没有过期，直接返回true
     *
     * @return true
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 帐号是否没有被锁定，直接返回true
     *
     * @return true
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 帐号证书是否没有过期，直接返回true
     *
     * @return true
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 帐号是否已经启用。直接返回true
     *
     * @return true
     */
    @Override
    public boolean isEnabled() {
        return true;
    }


    public boolean isAdmin() {
        return User.ADMIN.equals(this.getRole()) || User.SUPERADMIN.equals(this.getRole());
    }
}
