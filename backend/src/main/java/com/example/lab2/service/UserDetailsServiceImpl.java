package com.example.lab2.service;

import com.example.lab2.dao.UserRepository;
import com.example.lab2.dto.UserDTO;
import com.example.lab2.entity.User;
import com.example.lab2.exception.auth.password.NewPasswordIsOldPasswordException;
import com.example.lab2.exception.auth.PasswordException;
import com.example.lab2.exception.auth.RegisterException;
import com.example.lab2.exception.auth.password.PasswordContainUsernameException;
import com.example.lab2.exception.auth.password.PasswordTooWeakException;
import com.example.lab2.exception.auth.password.WrongPasswordException;
import com.example.lab2.exception.notfound.UserNotFoundException;
import com.example.lab2.request.auth.AddAdminRequest;
import com.example.lab2.request.auth.DeleteAdminRequest;
import com.example.lab2.response.GeneralResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * 这个类实现了Spring Security框架中UserDetailsService借口
 */
@Service("userService")
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;


    public GeneralResponse changePassword(String originalPassword, String newPassword, String username) {

        //查找是否有这个用户
        Optional<User> userOptional = userRepository.findByName(username);

        //用户不存在的情况
        if (!userOptional.isPresent()) {
            throw new UserNotFoundException("用户不存在！");
        }

        //从optional对象中获得真正的user
        User user = userOptional.get();

        //检查原来的密码是否输入正确
        if (!user.getPassword().equals(originalPassword)) {
            throw new WrongPasswordException("原密码输入不正确！");
        }

        if (newPassword.equals(user.getPassword())) {//密码不加密
            throw new NewPasswordIsOldPasswordException("新密码与旧密码相同");
        }
        if (newPassword.contains(username)) {
            throw new PasswordContainUsernameException("密码中不能包含帐号！");
        }

        if (!checkPasswordStrength(newPassword)) {
            throw new PasswordTooWeakException("密码中字母，数字，特殊字符必须包含至少两种。");
        }

        //如果能运行到这里，说明输入正常，无异常发生
        user.setPassword(newPassword);

        //存数据库
        userRepository.save(user);

        return new GeneralResponse("修改密码成功！");

    }

    private boolean checkPasswordStrength(String newPassword) {
        if (newPassword.length() < 6 || newPassword.length() > 32) {
            return false;
        }

        boolean[] bool = new boolean[3];
        int cnt = 0;
        for (int i = 0; i < newPassword.length(); i++) {
            char c = newPassword.charAt(i);
            if (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z') {
                bool[0] = true;
            } else if (c >= '0' && c <= '9') {
                bool[1] = true;
            } else if (c == '-' || c == '_') {
                bool[2] = true;
            }
        }
        for (boolean b : bool) {
            if (b) {
                cnt += 1;
            }
        }
        return cnt >= 2;
    }


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByName(s);
        if (user.isEmpty()) {
            return new User();
        }

        //用户的权限列表
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        //添加当前用户所有的权限（其实只有一个罢了）
        user.ifPresent(value -> authorities.add(new SimpleGrantedAuthority(value.getRole())));


        //将更新好的用户权限数组赋给用户对象
        user.orElse(null).setAuthorities(authorities);

        return user.orElse(null);


    }

    public HashMap<String, String> addAdmin(AddAdminRequest addAdminRequest) {
        //检测这个用户名是否已经在数据库中被使用过了
        if (userRepository.findByName(addAdminRequest.getUsername()).isPresent()) {
            throw new RegisterException("这个用户名已经被占用了，请换一个用户名");
        }

        //创建新的user对象
        User admin = new User(
                addAdminRequest.getUsername(),
                addAdminRequest.getPassword(),
                addAdminRequest.getEmail(),
                User.ADMIN,
                User.MAX_CREDIT
        );

        //把新创建的管理员存储到数据库中
        userRepository.save(admin);

        //设置成功时的信息
        HashMap<String, String> map = new HashMap<>();
        map.put("message", "添加管理员成功！");

        return map;


    }

    public GeneralResponse deleteAdmin(DeleteAdminRequest deleteAdminRequest) {
        //用户不存在
        Optional<User> user = userRepository.findByName(deleteAdminRequest.getUsername());
        if (!user.isPresent()) {
            throw new UserNotFoundException("管理员不存在！");
        }
        //用户是超级管理员
        if (user.get().getRole().equals("superadmin")) {
            throw new UserNotFoundException("此用户是超级管理员，不能删除！");
        }
        //用户不是admin
        if (!user.get().getRole().equals("admin")) {
            throw new UserNotFoundException("此用户不是管理员！");
        }

        //删除管理员
        userRepository.delete(user.get());
        return new GeneralResponse("删除管理员 " + deleteAdminRequest.getUsername() + " 成功");

    }

    public List<UserDTO> showAdmin() {
        return userRepository.getAllAdmin();
    }


}