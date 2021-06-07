package com.example.lab2.service;


import com.example.lab2.dao.CommentRepository;
import com.example.lab2.dao.ReplyRepository;
import com.example.lab2.dto.commentreply.CommentDTO;
import com.example.lab2.dto.commentreply.ReplyDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service("commentService")
public class CommentService {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    ReplyRepository replyRepository;


    /**
     * 根据isbn，查询这本书下面的所有评论和所有回复
     *
     * @param isbn    booktype的isbn，注意不是uniqueBookMark
     * @param isAdmin 正在执行操作的人是否是管理员
     * @return 见lab5讨论记录
     * @author zhj
     */
    public List<CommentDTO> getAllCommentAndReplyByISBN(String isbn, boolean isAdmin) {

        //根据输入的isbn，找到全部的评论列表，注意这里此时还没有注入List<ReplyDTO>
        List<CommentDTO> commentDTOList = commentRepository.getCommentsByIsbn(isbn);

        if (!isAdmin) {
            //如果正在操作的用户不是管理员，就过滤掉被管理员删除掉的评论，否则对管理员要显示被管理员删除（其实是屏蔽）的评论
            commentDTOList = commentDTOList.stream().filter(
                    commentDTO -> !commentDTO.isDeletedByAdmin()
            ).collect(Collectors.toList());
        }

        //查找对应的回复，添加到每个commentDTO里面去
        commentDTOList.forEach(commentDTO -> {

            //根据commentID查找对应的replyDTOList
            List<ReplyDTO> replyDTOList = replyRepository.getRepliesByCommentID(commentDTO.getCommentID());

            //如果用户不是管理员，就过滤掉被管理员删除的评论
            if (!isAdmin) {
                replyDTOList = replyDTOList.stream().filter(
                        replyDTO -> !replyDTO.isDeletedByAdmin()
                ).collect(Collectors.toList());
            }

            //将replyList注入对应的commentDTO里面去
            commentDTO.setReplyList(replyDTOList);

        });

        return commentDTOList;
    }
}
