package com.example.lab2.controller;


import com.example.lab2.dto.commentreply.CommentDTO;
import com.example.lab2.entity.Comment;
import com.example.lab2.service.CommentService;
import com.example.lab2.utils.JwtUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@RestController
public class UserAdminController {

    @Resource(name = "commentService")
    private CommentService commentService;


    /**
     * 根据isbn，向前端返回这种isbn的booktype下面的所有的评论和回复
     *
     * @param isbn 对应的书的isbn
     * @return 这种isbn的booktype下面的所有的评论和回复
     */
    @GetMapping("/useradmin/commentAndReply")
    public ResponseEntity<HashMap<String, Object>> getCommentAndReplyByIsbn(
            @RequestParam("isbn") String isbn,
            HttpServletRequest httpServletRequest) {

        //存储结果的hashmap
        HashMap<String, Object> res = new HashMap<>();

        //从http请求中获得header，并解析用户的权限
        String token = httpServletRequest.getHeader("token");

        //解析token，获得这个用户是否是管理员
        boolean isAdmin = JwtUtils.getIsAdmin(token);

        //进入业务层，获得commentList
        List<CommentDTO> commentList = commentService.getAllCommentAndReplyByISBN(isbn, isAdmin);

        //将获得的commentList存储到保存结果用的哈希表中
        res.put("commentList", commentList);

        //把请求发回给前端
        return ResponseEntity.ok(res);
    }


}
