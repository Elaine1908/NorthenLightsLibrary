package com.example.lab2.service;

import com.example.lab2.dao.UserConfigurationRepository;
import com.example.lab2.dto.UserConfigurationDTO;
import com.example.lab2.entity.UserConfiguration;
import com.example.lab2.exception.auth.SetConfigurationException;
import com.example.lab2.exception.notfound.UserNotFoundException;
import com.example.lab2.exception.notfound.UserTypeNotFoundException;
import com.example.lab2.request.auth.SetUserConfigurationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("userConfigurationService")
public class UserConfigurationService {

    @Autowired
    private UserConfigurationRepository userConfigurationRepository;


    public List<UserConfiguration> getAllUserConfiguration() {
        return userConfigurationRepository.findAll();
    }

    public List<UserConfigurationDTO> getAllUserConfigurationDTO() {
        List<UserConfiguration> userConfigurationList = userConfigurationRepository.findAll();
        return userConfigurationList.stream().map(
                UserConfigurationDTO::new
        ).collect(Collectors.toList());
    }

    //用于测试
    public Optional<UserConfiguration> getSingleUserConfiguration(String role) {
        return userConfigurationRepository.findUserConfigurationByRole(role);
    }

    public HashMap<String, String> setUserConfiguration(SetUserConfigurationRequest setUserConfigurationRequest) {

        Optional<UserConfiguration> userConfigurationOptional =
                userConfigurationRepository.findUserConfigurationByRole(
                        setUserConfigurationRequest.getRole()
                );
        //用户种类不存在
        if (userConfigurationOptional.isEmpty()) {
            throw new UserTypeNotFoundException("该用户种类不存在");
        }


        long maxBook = Long.parseLong(setUserConfigurationRequest.getMax_book_borrow());
        long maxBorrowTime = Long.parseLong(setUserConfigurationRequest.getMax_borrow_time());
        long maxReserveTime = Long.parseLong(setUserConfigurationRequest.getMax_reserve_time());

        UserConfiguration configuration = userConfigurationOptional.orElse(null);
        configuration.setMaxBookBorrow(maxBook);
        configuration.setMaxReserveTime(maxReserveTime);
        configuration.setMaxBorrowTime(maxBorrowTime);
        userConfigurationRepository.save(configuration);

        //设置成功时的信息
        HashMap<String, String> map = new HashMap<>();
        map.put("message", "设置成功！");

        return map;

    }
}
