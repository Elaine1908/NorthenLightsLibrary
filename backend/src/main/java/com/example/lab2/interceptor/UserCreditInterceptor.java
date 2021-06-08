package com.example.lab2.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.example.lab2.dao.UserRepository;
import com.example.lab2.entity.User;
import com.example.lab2.response.GeneralResponse;
import com.example.lab2.utils.JwtUtils;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class UserCreditInterceptor implements HandlerInterceptor {

    //当用户的信用大于等于这个数的时候，就能通过拦截器，否则就通不过拦截器，并提示用户信用不足
    private final int minCreditAllowed;

    private final UserRepository userRepository;


    //去哪里找用户名，和用户对应的信用。TOKEN表示在JWT里找，HTTP_SERVLET_REQUEST表示在HttpServletRequest里面的body的username里面找
    public enum WHERE_IS_USERNAME {
        TOKEN,
        HTTP_SERVLET_REQUEST
    }

    private final WHERE_IS_USERNAME where_is_username;

    private String getUserName(HttpServletRequest httpServletRequest) {
        if (where_is_username == WHERE_IS_USERNAME.TOKEN) {//从token得到用户名
            //从请求头得到token
            String token = httpServletRequest.getHeader("token");
            if (token == null) {
                return null;
            }

            //从token得到用户名
            String username = JwtUtils.getUserName(token);
            return username;
        } else {//从HTTP请求体得到用户名
            try {
                //将请求体中的username属性映射到user对象上
                ObjectMapper objectMapper=new ObjectMapper();
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                User user = objectMapper.readValue(httpServletRequest.getInputStream(), User.class);
                return user.getUsername();
            } catch (IOException ioException) {
                return null;
            }

        }

    }

    public UserCreditInterceptor(int minCreditAllowed, UserRepository userRepository, WHERE_IS_USERNAME where_is_username) {
        this.minCreditAllowed = minCreditAllowed;
        this.userRepository = userRepository;
        this.where_is_username = where_is_username;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {


        //由于拦截器里没有SpringBoot的封装，只能按照最基本的JavaWeb的方法来处理请求
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        ServletOutputStream out = response.getOutputStream();


        //尝试得到用户名
        String username = this.getUserName(request);

        //如果得不到用户名，就提示用户未登录
        if (username == null) {
            GeneralResponse generalResponse = new GeneralResponse("未登录");
            out.write(JSONObject.toJSONString(generalResponse).getBytes(StandardCharsets.UTF_8));
            response.setStatus(400);
            return false;
        }

        //从数据库中得到用户
        User user = userRepository.getUserByUsername(username);

        if (user == null) {
            GeneralResponse generalResponse = new GeneralResponse("找不到用户");
            out.write(JSONObject.toJSONString(generalResponse).getBytes(StandardCharsets.UTF_8));
            response.setStatus(400);
            return false;
        }

        //如果用户信用太小，就拦截，并给出用户提示
        if (user.getCredit() < minCreditAllowed) {
            String msg = String.format(
                    "此功能要求用户的信用为至少%d，用户%s的信用为%d，请先想方法提升信用",
                    minCreditAllowed,
                    user.getUsername(),
                    user.getCredit()
            );
            response.setStatus(403);
            out.write(JSONObject.toJSONString(new GeneralResponse(msg)).getBytes(StandardCharsets.UTF_8));
            return false;
        }

        //如果信用符合要求，就放行
        return true;

    }
}
