package com.example.lab2.controller;

import com.example.lab2.dto.BorrowRecordDTO;
import com.example.lab2.dto.ReserveRecordDTO;
import com.example.lab2.dto.ReturnRecordDTO;
import com.example.lab2.entity.Borrow;
import com.example.lab2.entity.Fine;
import com.example.lab2.request.fine.PayFineRequest;
import com.example.lab2.response.GeneralResponse;
import com.example.lab2.service.BorrowService;

import com.example.lab2.service.FineService;
import com.example.lab2.service.NormalUserService;
import com.example.lab2.service.ReserveService;
import com.example.lab2.utils.JwtUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/user")
public class NormalUserController {

    @Resource(name = "normalUserService")
    NormalUserService normalUserService;

    @Resource(name = "fineService")
    FineService fineService;

    @GetMapping("/userinfo")
    public ResponseEntity<?> userInfo(HttpServletRequest request) {
        String token = request.getHeader("token");
        String username = JwtUtils.getUserName(token);
        return ResponseEntity.ok(normalUserService.userInfo(username));
    }


    /**
     * 得到用户的全部罚款
     *
     * @return
     * @author zhj
     */
    @GetMapping("/myfine")
    public ResponseEntity<HashMap<String, List<Fine>>> getMyFine(HttpServletRequest httpServletRequest) {

        //在header中获得token，再从token中解析出用户名
        String token = httpServletRequest.getHeader("token");
        String username = JwtUtils.getUserName(token);

        List<Fine> fineList = fineService.getAllFine(username);

        HashMap<String, List<Fine>> res = new HashMap<>();
        res.put("fineList", fineList);
        return ResponseEntity.ok(res);

    }

    /**
     * 用户查看自己所有的历史记录
     * @author zyw
     */
    @GetMapping("/myRecord")
    public ResponseEntity<HashMap<String,Object>> getMyRecord(HttpServletRequest httpServletRequest){
        //在header中获得token，再从token中解析出用户名
        String token = httpServletRequest.getHeader("token");
        String username = JwtUtils.getUserName(token);
        List<ReserveRecordDTO> reserveRecordDTOS = normalUserService.getReserveRecord(username);
        List<BorrowRecordDTO> borrowRecordDTOS = normalUserService.getBorrowRecord(username);
        List<ReturnRecordDTO> returnRecordDTOS = normalUserService.getReturnRecord(username);



        //加入result
        HashMap<String, Object> result = new HashMap<>();
        result.put("reserveRecordList",reserveRecordDTOS);
        result.put("borrowRecordList",borrowRecordDTOS);
        result.put("returnRecordList",returnRecordDTOS);


        return ResponseEntity.ok(result);

    }
    /**
     * 用户交罚款的接口
     *
     * @author zhj
     */
    @PostMapping("/payfine")
    public ResponseEntity<GeneralResponse> payfine(
            @RequestBody PayFineRequest payFineRequest,
            HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("token");
        String username = JwtUtils.getUserName(token);

        GeneralResponse generalResponse = fineService.payfine(username, payFineRequest.getFineID());

        return ResponseEntity.ok(generalResponse);

    }



}
