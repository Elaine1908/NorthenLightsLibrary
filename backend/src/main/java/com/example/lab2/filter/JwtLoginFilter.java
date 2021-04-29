package com.example.lab2.filter;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.jaxrs.FastJsonProvider;
import com.example.lab2.entity.User;
import com.example.lab2.request.auth.LoginRequest;
import com.example.lab2.response.GeneralResponse;
import com.example.lab2.utils.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 用户登陆时会经过这个过滤器
 */
public class JwtLoginFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    public JwtLoginFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
        this.authenticationManager = authenticationManager;
        this.setFilterProcessesUrl("/auth/login");
    }


    /**
     * 用户尝试登陆的时候会经过这个函数
     *
     * @param request
     * @param response
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {
            User user = new ObjectMapper().readValue(request.getInputStream(), User.class);
            request.setAttribute("libraryID", user.getLibraryID());

            //尝试登录
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUsername(),
                            user.getPassword(),
                            new ArrayList<>()
                    )
            );

            //获得登陆后的用户用户
            User loginUser = (User) authentication.getPrincipal();

            //如果是管理员的话，前端必须输入libraryID
            Long libraryID = user.getLibraryID();
            if ((loginUser.getRole().equals(User.ADMIN) || loginUser.getRole().equals(User.SUPERADMIN))
                    && libraryID == null) {
                throw new AuthenticationException("管理员登录时必须选择工作的图书馆！") {
                    @Override
                    public String getMessage() {
                        return super.getMessage();
                    }
                };
            }

            return authentication;


        } catch (IOException e) {
            throw new AuthenticationException("未知错误") {
                @Override
                public String getMessage() {
                    return super.getMessage();
                }
            };
        }

    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        User user = (User) authResult.getPrincipal();
        String role = user.getRole();

        //如果用户是管理员，还要设置他的上班地点
        if (user.getRole().equals(User.ADMIN) || user.getRole().equals(User.SUPERADMIN)) {
            user.setLibraryID((Long) request.getAttribute("libraryID"));
        }

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        String jsonString = JSONObject.toJSONString(new GeneralResponse(role));
        response.getWriter().write(jsonString);

        String token = JwtUtils.generateJwt(user);
        response.addHeader("token", token);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setStatus(403);
        response.setContentType("application/json; charset=utf-8");

        String jsonString = JSONObject.toJSONString(new GeneralResponse(failed.getMessage()));
        response.getWriter().write(jsonString);
    }
}
