package com.example.lab2.service;

import com.example.lab2.dao.CommentRepository;
import com.example.lab2.dao.ReplyRepository;
import com.example.lab2.dao.UserRepository;
import com.example.lab2.dto.commentreply.CommentDTO;
import com.example.lab2.dto.commentreply.ReplyDTO;
import com.example.lab2.entity.Comment;
import com.example.lab2.entity.Library;
import com.example.lab2.entity.Reply;
import com.example.lab2.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CommentServiceTest {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    ReplyRepository replyRepository;

    @Autowired
    UserRepository userRepository;

    @Resource(name = "commentService")
    CommentService commentService;

    @Test
    @Transactional
    public void testGetAllCommentAndReplyByISBN() {
        User u1 = new User(
                "testUserOne",
                "testPassword",
                "zhj630985214@gmail.com",
                User.POSTGRADUATE,
                100
        );


        User u2 = new User(
                "testUserTwo",
                "testPassword",
                "zhj630985214@gmail.com",
                User.POSTGRADUATE,
                100
        );

        userRepository.save(u1);
        userRepository.save(u2);

        Comment comment = new Comment(u1.getUser_id(),
                "testIsbn",
                "testContent",
                new Date(),
                false,
                false,
                10
        );
        commentRepository.save(comment);

        for (int i = 0; i < 10; i++) {
            Reply reply = new Reply(
                    u2.getUser_id(),
                    comment.getCommendID(),
                    "testContent",
                    new Date(),
                    false,
                    false,
                    u1.getUser_id()
            );
            if (i < 3) {
                reply.setDeletedByAdmin(true);
            }
            replyRepository.save(reply);

        }

        List<CommentDTO> commentDTOList = commentService.getAllCommentAndReplyByISBN("testIsbn", true);
        CommentDTO commentDTO = commentDTOList.get(0);
        assertEquals(commentDTO.getUsername(), "testUserOne");
        assertEquals(commentDTO.getReplyList().size(), 10);
        for (int i = 0; i < commentDTO.getReplyList().size(); i++) {
            ReplyDTO rp = commentDTO.getReplyList().get(i);
            assertEquals(rp.getUsername(), "testUserTwo");
            assertEquals(rp.getRepliedUsername(), "testUserOne");
        }


        commentDTOList = commentService.getAllCommentAndReplyByISBN("testIsbn", false);
        assertEquals(commentDTOList.get(0).getReplyList().size(), 7);

    }
}