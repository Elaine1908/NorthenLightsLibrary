package com.example.lab2.service;

import com.example.lab2.dao.UserRepository;
import com.example.lab2.entity.User;
import com.example.lab2.exception.LoginException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 这个类实现了Spring Security框架中UserDetailsService借口
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    public Optional<User> selectUserByName(String name) {
        return userRepository.findByName(name);
    }

    public User getUser(String username, String password) {
        return userRepository.getUserByUsernameAndPassword(username, password);
    }

    public void save(User user) throws SQLIntegrityConstraintViolationException {
        userRepository.save(user);
    }

    public User login(String username, String password) {
        User user = userRepository.getUserByUsername(username);
        if (user == null) {//用户没有注册
            throw new LoginException("用户名或密码错误");
        }
        String user_pass_md5 = user.getPassword();
        String input_pass_md5 = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        if (!user_pass_md5.equals(input_pass_md5)) {
            throw new LoginException("用户名或密码错误");
        }
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByName(s);
        if (!user.isPresent()) {
            return null;
        }

        //用户的权限列表
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        //添加当前用户所有的权限（其实只有一个罢了）
        authorities.add(new SimpleGrantedAuthority(user.get().getRole()));

        //将更新好的用户权限数组赋给用户对象
        user.get().setAuthorities(authorities);

        return user.get();

    }


}