package com.example.lab2.service;

import com.example.lab2.dao.UserRepository;
import com.example.lab2.entity.User;
import com.example.lab2.exception.auth.RegisterException;
import com.example.lab2.exception.auth.password.NewPasswordIsOldPasswordException;
import com.example.lab2.exception.auth.password.PasswordContainUsernameException;
import com.example.lab2.exception.auth.password.PasswordTooWeakException;
import com.example.lab2.exception.auth.password.WrongPasswordException;
import com.example.lab2.request.auth.AddAdminRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.sql.SQLIntegrityConstraintViolationException;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class UserDetailsServiceImplTest {

    @Resource(name = "userService")
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    UserRepository userRepository;

    @Test
    @Transactional
    public void testLoadUserByUsername() throws SQLIntegrityConstraintViolationException {
        User user = new User("luanqibazao", "luanqibazao", "email@email.com", User.STUDENT, User.MAX_CREDIT);

        userRepository.save(user);

        User userFromDB = (User) userDetailsService.loadUserByUsername("luanqibazao");

        assertEquals(userFromDB.getUsername(), "luanqibazao");
        assertEquals(userFromDB.getPassword(), "luanqibazao");
        assertEquals(userFromDB.getEmail(), "email@email.com");
        assertEquals(userFromDB.getRole(), User.STUDENT);
        assertEquals(userFromDB.getCredit(), User.MAX_CREDIT);
    }

    @Test
    @Transactional
    public void testAddAdmin() {

        AddAdminRequest addAdminRequest = new AddAdminRequest("adminusername", "adminpassword", "email@email.com");

        //正常添加
        userDetailsService.addAdmin(addAdminRequest);

        User user = userRepository.findByName("adminusername").get();

        assertEquals(user.getUsername(), "adminusername");
        assertEquals(user.getPassword(), "adminpassword");
        assertEquals(user.getEmail(), "email@email.com");


        //用户名重复添加
        assertThrows(RegisterException.class, () -> {
            userDetailsService.addAdmin(addAdminRequest);
        });


    }

    @Test
    public void testCheckPasswordStrength() throws Exception {

        //私有方法只能反射调用了。。。
        Method checkPasswordStrengthMethod = UserDetailsServiceImpl.class.getDeclaredMethod("checkPasswordStrength", String.class);

        checkPasswordStrengthMethod.setAccessible(true);

        assertFalse((Boolean) checkPasswordStrengthMethod.invoke(userDetailsService, "000"));
        assertFalse((Boolean) checkPasswordStrengthMethod.invoke(userDetailsService,
                "duhfohfoiwjefpi.woeijf088709890fjpwef"));
        assertTrue((Boolean) checkPasswordStrengthMethod.invoke(userDetailsService,
                "ZHJ20001006"));
        assertFalse((Boolean) checkPasswordStrengthMethod.invoke(userDetailsService,
                "12345678"));
        assertFalse((Boolean) checkPasswordStrengthMethod.invoke(userDetailsService,
                "ABCDEFGH"));
        assertFalse((Boolean) checkPasswordStrengthMethod.invoke(userDetailsService,
                "---------"));
        assertTrue((Boolean) checkPasswordStrengthMethod.invoke(userDetailsService,
                "6---------"));
    }


    @Transactional
    @Test
    public void testChangePassword_OldPasswordIsNewPassword() {
        User user = new User(
                "newUser",
                "password",
                "zhj@email.com",
                User.STUDENT,
                User.MAX_CREDIT
        );


        userRepository.save(user);

        assertThrows(NewPasswordIsOldPasswordException.class, () -> {
            userDetailsService.changePassword("password", "password", "newUser");
        });

    }


    @Transactional
    @Test
    public void testChangePassword_OldPasswordWrong() {
        User user = new User(
                "newUser",
                "password",
                "zhj@email.com",
                User.STUDENT,
                User.MAX_CREDIT
        );


        userRepository.save(user);

        assertThrows(WrongPasswordException.class, () -> {
            userDetailsService.changePassword("passwofwfwfewefrd", "password", "newUser");
        });

    }

    @Transactional
    @Test
    public void testChangePassword_PasswordToWeak() {
        User user = new User(
                "newUser",
                "password",
                "zhj@email.com",
                User.STUDENT,
                User.MAX_CREDIT
        );


        userRepository.save(user);

        assertThrows(PasswordTooWeakException.class, () -> {
            userDetailsService.changePassword("password", "000000", "newUser");
        });

    }


    @Transactional
    @Test
    public void testChangePassword_PasswordContainUsername() {
        User user = new User(
                "newUser",
                "password",
                "zhj@email.com",
                User.STUDENT,
                User.MAX_CREDIT
        );


        userRepository.save(user);

        assertThrows(PasswordContainUsernameException.class, () -> {
            userDetailsService.changePassword("password", "newUserrr123", "newUser");
        });

    }

    @Transactional
    @Test
    public void testChangePassword_Success() {
        User user = new User(
                "newUser",
                "password",
                "zhj@email.com",
                User.STUDENT,
                User.MAX_CREDIT
        );


        userRepository.save(user);

        userDetailsService.changePassword("password", "Zhj001006", "newUser");

        User userFromDB = userRepository.findByName("newUser").get();

        assertEquals(userFromDB.getUsername(), "newUser");

        assertEquals(userFromDB.getPassword(), "Zhj001006");

        assertEquals(userFromDB.getEmail(), "zhj@email.com");

        assertEquals(userFromDB.getRole(), User.STUDENT);

        assertEquals(userFromDB.getCredit(), User.MAX_CREDIT);
    }


}