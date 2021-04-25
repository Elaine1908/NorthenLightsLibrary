package com.example.lab2.config;

import com.alibaba.fastjson.JSONObject;
import com.example.lab2.response.GeneralResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 这个类是用于返回请求被spring security拦截后显示自定义的信息，而不是自带的默认信息的
 */
public class MyAuthenticationFailEntryPointImpl implements AuthenticationEntryPoint {

    public MyAuthenticationFailEntryPointImpl() {
    }

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        GeneralResponse response = new GeneralResponse("你当前访问的接口权限不足，请检查后再试");
        String jsonMessage = JSONObject.toJSONString(response);

        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.getWriter().write(jsonMessage);

        httpServletResponse.setStatus(403);

    }
}
