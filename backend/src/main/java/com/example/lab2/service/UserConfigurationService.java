package com.example.lab2.service;

import com.example.lab2.dao.UserConfigurationRepository;
import com.example.lab2.entity.UserConfiguration;
import com.example.lab2.exception.notfound.UserTypeNotFoundException;
import com.example.lab2.request.auth.SetUserConfigurationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service("userConfigurationService")
public class UserConfigurationService {

    @Autowired
    private UserConfigurationRepository userConfigurationRepository;


    public List<UserConfiguration> getAllUserConfiguration() {
        return userConfigurationRepository.findAll();
    }

    //用于测试
    public Optional<UserConfiguration> getSingleUserConfiguration(String role){return userConfigurationRepository.findUserConfigurationByRole(role);}

    public HashMap<String, String> setUserConfiguration(SetUserConfigurationRequest setUserConfigurationRequest){

        //用户种类不存在
        if(userConfigurationRepository.findUserConfigurationByRole(setUserConfigurationRequest.getRole()).isEmpty()){
            throw new UserTypeNotFoundException("该用户种类不存在");
        }

        long maxBook = Long.parseLong(setUserConfigurationRequest.getMax_book_borrow());
        long maxBorrow = Long.parseLong(setUserConfigurationRequest.getMax_book_borrow());
        long maxReserve = Long.parseLong(setUserConfigurationRequest.getMax_reserve_time());

        UserConfiguration configuration = new UserConfiguration(setUserConfigurationRequest.getRole(),maxBook,maxBorrow,maxReserve);
        userConfigurationRepository.save(configuration);

        //设置成功时的信息
        HashMap<String, String> map = new HashMap<>();
        map.put("message", "设置成功！");

        return map;

    }
}
