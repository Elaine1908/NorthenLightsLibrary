package com.example.lab2.service;

import com.example.lab2.entity.Library;
import com.example.lab2.utils.SensitiveWordChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("adminService")
public class AdminService {

    @Autowired
    SensitiveWordChecker sensitiveWordChecker;

    public List<String> findAllSensitiveWord() {
        List<String> sensitiveWordList =
                new ArrayList<>(sensitiveWordChecker.getSensitiveWords());
        return sensitiveWordList;
    }

    public String addToSensitive(List<String> addToSensitiveList) {
        sensitiveWordChecker.addToSensitiveList(addToSensitiveList);
        return "添加成功";
    }

    public String removeFromSensitive(List<String> removeFromSensitiveList) {
        sensitiveWordChecker.removeFromSensitiveList(removeFromSensitiveList);
        return "移除成功";
    }
}
