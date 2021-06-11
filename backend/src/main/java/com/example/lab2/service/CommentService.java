package com.example.lab2.service;


import com.example.lab2.dao.CommentRepository;
import com.example.lab2.dao.ReplyRepository;
import com.example.lab2.dao.UserRepository;
import com.example.lab2.dto.commentreply.CommentDTO;
import com.example.lab2.dto.commentreply.ReplyDTO;
import com.example.lab2.entity.Comment;
import com.example.lab2.entity.Reply;
import com.example.lab2.entity.User;
import com.example.lab2.exception.auth.RoleNotAllowedException;
import com.example.lab2.exception.notfound.CommentNotFoundException;
import com.example.lab2.exception.notfound.UserNotFoundException;
import com.example.lab2.response.GeneralResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("commentService")
public class CommentService {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    UserRepository userRepository;

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

    /**
     * 管理员删除评论
     *
     * @param commentID
     * @param username
     * @author yiwen
     */
    public GeneralResponse deleteComment(Long commentID, String username) {
        //检查用户存不存在，并得到用户的userID
        Optional<User> userOptional = userRepository.findByName(username);
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("管理员不存在");
        }

        if (!userOptional.orElse(null).getRole().equals(User.ADMIN) && !userOptional.get().getRole().equals(User.SUPERADMIN)) {
            throw new RoleNotAllowedException("你不是管理员");
        }
        //看看评论是否存在
        Optional<Comment> commentOptional = commentRepository.findById(commentID);
        if (commentOptional.isEmpty()) {
            throw new CommentNotFoundException("这条评论不存在");
        } else {
            Comment comment = commentOptional.orElse(null);
            comment.setDeletedByAdmin(true);//删除评论
            commentRepository.save(comment);
        }


        return new GeneralResponse("删除评论成功");
    }

    /**
     * 管理员删除回复
     *
     * @param replyID
     * @param username
     * @author yiwen
     */
    public GeneralResponse deleteReply(Long replyID, String username) {
        //检查用户存不存在，并得到用户的userID
        Optional<User> userOptional = userRepository.findByName(username);
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("管理员不存在");
        }

        if (!userOptional.orElse(null).getRole().equals(User.ADMIN) && !userOptional.get().getRole().equals(User.SUPERADMIN)) {
            throw new RoleNotAllowedException("你不是管理员");
        }

        //看看回复是否存在
        Optional<Reply> replyOptional = replyRepository.findById(replyID);
        if (replyOptional.isEmpty()) {
            throw new CommentNotFoundException("这条回复不存在");
        } else {
            Reply reply = replyOptional.orElse(null);
            reply.setDeletedByAdmin(true);//删除回复
            replyRepository.save(reply);
        }


        return new GeneralResponse("删除回复成功");
    }
}
