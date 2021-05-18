package com.example.lab2.service;

import com.example.lab2.entity.UserConfiguration;
import com.example.lab2.exception.auth.RegisterException;
import com.example.lab2.exception.auth.SetConfigurationException;
import com.example.lab2.exception.notfound.UserTypeNotFoundException;
import com.example.lab2.request.auth.SetUserConfigurationRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import java.util.List;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserConfigurationServiceTest {

    @Resource(name = "userConfigurationService")
    UserConfigurationService userConfigurationService;

    @Test
    public void testGetAllUserConfiguration() {
        List<UserConfiguration> userConfigurationList = userConfigurationService.getAllUserConfiguration();
        userConfigurationList.sort((UserConfiguration uc1, UserConfiguration uc2) -> {
            return uc1.getRole().compareTo(uc2.getRole());
        });
        assertEquals(userConfigurationList.size(), 3);
        assertEquals(userConfigurationList.get(0).getRole(), "postgraduate");
        assertEquals(userConfigurationList.get(1).getRole(), "teacher");
        assertEquals(userConfigurationList.get(2).getRole(), "undergraduate");


    }

    @Test
    @Transactional
    public void testSetUserConfiguration_NonExistentRole(){
        SetUserConfigurationRequest userConfigurationRequest = new SetUserConfigurationRequest("a","10","360","360");
        assertThrows(UserTypeNotFoundException.class, () -> {
            userConfigurationService.setUserConfiguration(userConfigurationRequest);
        });
    }

    @Test
    @Transactional
    public void testSetUserConfiguration_InvalidParam(){
        SetUserConfigurationRequest userConfigurationRequest = new SetUserConfigurationRequest("teacher","10","0360","360");
        assertThrows(SetConfigurationException.class, () -> {
            userConfigurationService.setUserConfiguration(userConfigurationRequest);
        });
    }

    @Test
    @Transactional
    public void testSetUserConfiguration_success_undergraduate(){
        SetUserConfigurationRequest userConfigurationRequest = new SetUserConfigurationRequest("undergraduate","8","1296000","1298000");
        userConfigurationService.setUserConfiguration(userConfigurationRequest);
        UserConfiguration conf = userConfigurationService.getSingleUserConfiguration("undergraduate").get();

        assertEquals(conf.getMaxBookBorrow(),8);
        assertEquals(conf.getMaxBorrowTime(),1296000);
        assertEquals(conf.getMaxReserveTime(),1298000);
    }

    @Test
    @Transactional
    public void testSetUserConfiguration_success_postgraduate(){
        SetUserConfigurationRequest userConfigurationRequest = new SetUserConfigurationRequest("postgraduate","8","1296000","1298000");
        userConfigurationService.setUserConfiguration(userConfigurationRequest);
        UserConfiguration conf = userConfigurationService.getSingleUserConfiguration("postgraduate").get();

        assertEquals(conf.getMaxBookBorrow(),8);
        assertEquals(conf.getMaxBorrowTime(),1296000);
        assertEquals(conf.getMaxReserveTime(),1298000);
    }

    @Test
    @Transactional
    public void testSetUserConfiguration_success_teacher(){
        SetUserConfigurationRequest userConfigurationRequest = new SetUserConfigurationRequest("teacher","8","1296000","1298000");
        userConfigurationService.setUserConfiguration(userConfigurationRequest);
        UserConfiguration conf = userConfigurationService.getSingleUserConfiguration("teacher").get();

        assertEquals(conf.getMaxBookBorrow(),8);
        assertEquals(conf.getMaxBorrowTime(),1296000);
        assertEquals(conf.getMaxReserveTime(),1298000);
    }

}