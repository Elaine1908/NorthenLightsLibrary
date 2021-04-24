package com.example.lab2.service;

import com.example.lab2.dao.UserRepository;
import com.example.lab2.dto.UserDTO;
import com.example.lab2.entity.User;
import com.example.lab2.exception.LoginException;
import com.example.lab2.exception.PasswordException;
import com.example.lab2.exception.RegisterException;
import com.example.lab2.request.auth.AddAdminRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
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
    private static UserRepository userRepositoryStatic;

    @PostConstruct
    public void init() {
        userRepositoryStatic = userRepository;
    }

    public  Optional<User> selectUserByName(String name) {
        return userRepositoryStatic.findByName(name);
    }


    public static void save(User user) throws SQLIntegrityConstraintViolationException {
        userRepositoryStatic.save(user);
    }


    public void changePassword(String newPassword,String username){
        User user = userRepository.getUserByUsername(username);
        //String pass_md5 = DigestUtils.md5DigestAsHex(newPassword.getBytes(StandardCharsets.UTF_8));
        if(newPassword.equals(user.getPassword())){//密码不加密
            throw new PasswordException("新密码与旧密码相同");
        }else if (newPassword.contains(username)){
            throw new PasswordException("密码中不能包含帐号！");
        }else if(!checkPasswordStrength(newPassword)){
            throw new PasswordException("密码中字母，数字，特殊字符必须包含至少两种。");
        }
        userRepository.updatePasswordByUsername(newPassword,username);
    }

    private boolean checkPasswordStrength(String newPassword) {
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

    public HashMap<String, String> addAdmin(AddAdminRequest addAdminRequest) {
        //检测这个用户名是否已经在数据库中被使用过了
        if (this.selectUserByName(addAdminRequest.getUsername()).isPresent()) {
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
        try {
            UserDetailsServiceImpl.save(admin);
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new RegisterException("添加管理员失败");
        }

        //设置成功时的信息
        HashMap<String, String> map = new HashMap<>();
        map.put("message", "添加管理员成功！");

        return map;


    }

    public List<UserDTO> showAdmin(){
        return userRepository.getAllAdmin();
    }
}