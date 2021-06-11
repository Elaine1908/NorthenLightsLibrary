package com.example.lab2.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.example.lab2.response.GeneralResponse;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;

public class CommentReplyInterceptor implements HandlerInterceptor {

    private static final int MAX_LENGTH_ALLOWED = 1000;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        //将http请求中的content属性映射到ContentObj里面去
        ContentObj contentObj = objectMapper.readValue(request.getInputStream(), ContentObj.class);

        //得到content
        String content = contentObj.getContent();

        response.setContentType("application/json");

        //检查长度是否符合太长
        if (content.length() > MAX_LENGTH_ALLOWED) {//如果长度太大，就提示前端错误
            String msg = String.format(
                    "系统允许的评论/回复最大长度为%d，你的评论/回复长度为%d", MAX_LENGTH_ALLOWED, content.length());
            response.getOutputStream().write(
                    JSONObject.toJSONString(new GeneralResponse(msg)).getBytes(StandardCharsets.UTF_8)
            );
            response.setStatus(400);
            return false;
        }

        //检查内容是否为空
        if (content.isBlank()) {//如果为空，就提示前端错误
            String msg = "回复不能为空";
            response.getOutputStream().write(
                    JSONObject.toJSONString(new GeneralResponse(msg)).getBytes(StandardCharsets.UTF_8)
            );
            response.setStatus(400);
            return false;
        }

        //检查敏感词
        //todo

        return true;


    }


    /**
     * 仅有一个content属性的简单对象，用于CommentReplyInterceptor将HttpBody映射
     */
    @NoArgsConstructor
    @Getter
    @Setter
    static class ContentObj {
        String content;

    }
}
