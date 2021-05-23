package com.example.lab2.service;

import com.example.lab2.dao.record.FineRecordRepository;
import com.example.lab2.dao.FineRepository;
import com.example.lab2.dao.UserRepository;
import com.example.lab2.entity.Fine;
import com.example.lab2.entity.FineRecord;
import com.example.lab2.entity.User;
import com.example.lab2.exception.notfound.FineNotFoundException;
import com.example.lab2.exception.notfound.UserNotFoundException;
import com.example.lab2.request.fine.PayFineRequestToTAServer;
import com.example.lab2.request.fine.PayFineResponseToTAServer;
import com.example.lab2.response.GeneralResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service("fineService")
public class FineService {


    @Autowired
    FineRepository fineRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    FineRecordRepository fineRecordRepository;

    @Autowired
    RestTemplate restTemplate;

    /**
     * @param username 用户名
     * @return
     * @author zhj
     * 根据用户名，获得用户的全部罚款
     */
    public List<Fine> getAllFine(String username) {
        //先根据用户名查找用户，找出userID
        Optional<User> userOptional = userRepository.findByName(username);
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("找不到用户");
        }
        long userID = userOptional.get().getUser_id();

        //根据userID查罚款
        List<Fine> fineList = fineRepository.getFineByUserID(userID);

        return fineList;
    }

    /**
     * 用户缴纳罚款的Service层
     *
     * @param username
     * @param fineID
     * @return
     * @author zhj
     */
    public GeneralResponse payfine(String username, long fineID) {

        //检查用户存不存在，并得到用户的useid
        Optional<User> userOptional = userRepository.findByName(username);
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("找不到这个用户");
        }
        Optional<Fine> fineOptional = fineRepository.findById(fineID);
        if (fineOptional.isEmpty()) {
            throw new FineNotFoundException("找不到这次罚款");
        }

        //尝试朝TA的服务器发起请求
        PayFineResponseToTAServer payFineResponseToTAServer = payFineToTAServer(
                userOptional.get().getUsername(),
                fineOptional.get().getMoney(),
                fineOptional.get());

        return new GeneralResponse(payFineResponseToTAServer.getMsg());

    }

    private PayFineResponseToTAServer payFineToTAServer(String username, long amount, Fine fine) {
        PayFineRequestToTAServer request = new PayFineRequestToTAServer(
                "se2021_20",
                username,
                amount
        );

        try {
            //尝试向TA的服务器发起请求
            PayFineResponseToTAServer response = restTemplate.postForObject(
                    "http://47.103.205.96:8080/api/payment",
                    request,
                    PayFineResponseToTAServer.class
            );

            //请求成功的情况，删除对应的罚款，更新对应的罚款记录
            fineRepository.delete(fine);

            //根据uuid得到对应的罚款记录
            String uuid = fine.getUuid();
            Optional<FineRecord> fineRecordOptional = fineRecordRepository.getFineRecordByUuid(uuid);
            if (fineRecordOptional.isPresent()) {
                fineRecordOptional.get().setStatus(FineRecord.PAID);
                fineRecordRepository.save(fineRecordOptional.get());
            }

            return response;

        } catch (HttpClientErrorException e) {
            //请求失败的情况，可能是余额不足等原因
            if (e.getStatusCode().equals(HttpStatus.CONFLICT)) {
                return new PayFineResponseToTAServer("余额不足");
            } else {
                return new PayFineResponseToTAServer("未知错误");
            }

        }
    }

}
