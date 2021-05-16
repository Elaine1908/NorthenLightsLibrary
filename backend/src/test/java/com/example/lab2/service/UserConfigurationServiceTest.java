package com.example.lab2.service;

import com.example.lab2.entity.UserConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.util.List;

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

}