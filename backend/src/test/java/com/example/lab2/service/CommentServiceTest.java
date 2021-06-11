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
import com.example.lab2.exception.auth.RoleNotAllowedException;
import com.example.lab2.exception.notfound.CommentNotFoundException;
import com.example.lab2.exception.notfound.UserNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
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

    @Test
    @Transactional
    public void testDeleteComment_NonExistentAdmin(){
        assertThrows(UserNotFoundException.class, () -> {
            commentService.deleteComment((long)1,"non_existent_admin");
        });
    }

    @Test
    @Transactional
    public void testDeleteComment_NotAdmin(){
        User user = new User(
                "newUser",
                "password",
                "zyw@email.com",
                User.STUDENT,
                User.MAX_CREDIT
        );
        userRepository.save(user);
        assertThrows(RoleNotAllowedException.class, () -> {
            commentService.deleteComment((long)8888,"newUser");
        });
    }

    @Test
    @Transactional
    public void testDeleteComment_CommentNotFound(){
        User user = new User(
                "newUser",
                "password",
                "zyw@email.com",
                User.ADMIN,
                User.MAX_CREDIT
        );
        userRepository.save(user);
        assertThrows(CommentNotFoundException.class, () -> {
            commentService.deleteComment((long)8888,"newUser");
        });
    }

    @Test
    @Transactional
    public void testDeleteComment_Success(){
        User user = new User(
                "newUser",
                "password",
                "zyw@email.com",
                User.STUDENT,
                User.MAX_CREDIT
        );
        userRepository.save(user);
        User userFromDB = userRepository.getUserByUsername("newUser");
        User admin = new User(
                "newAdmin",
                "password",
                "zyw@email.com",
                User.ADMIN,
                User.MAX_CREDIT
        );
        userRepository.save(admin);

        Comment comment = new Comment(userFromDB.getUser_id(),"isbn","good",new Date(),false,false,8);
        commentRepository.save(comment);
        Optional<Comment> commentFromDB = commentRepository.getCommentByUserID(userFromDB.getUser_id());
        assertTrue(commentFromDB.isPresent());
        commentService.deleteComment(commentFromDB.get().getCommendID(),"newAdmin");
        Optional<Comment> commentFromDB2 = commentRepository.getCommentByUserID(userFromDB.getUser_id());
        assertFalse(commentFromDB2.isPresent());

    }

    @Test
    @Transactional
    public void testDeleteReply_ReplyNotFound(){
        User user = new User(
                "newUser",
                "password",
                "zyw@email.com",
                User.ADMIN,
                User.MAX_CREDIT
        );
        userRepository.save(user);

        assertThrows(CommentNotFoundException.class, () -> {
            commentService.deleteReply((long)8888,"newUser");
        });
    }

    @Test
    @Transactional
    public void testDeleteReply_Success(){
        User admin = new User(
                "newAdmin",
                "password",
                "zyw@email.com",
                User.ADMIN,
                User.MAX_CREDIT
        );
        userRepository.save(admin);

        User user = new User(
                "newUser",
                "password",
                "zyw@email.com",
                User.STUDENT,
                User.MAX_CREDIT
        );
        userRepository.save(user);
        User userFromDB = userRepository.getUserByUsername("newUser");

        Comment comment = new Comment(userFromDB.getUser_id(),"isbn","good",new Date(),false,false,8);
        commentRepository.save(comment);
        Optional<Comment> commentFromDB = commentRepository.getCommentByUserID(userFromDB.getUser_id());
        assertTrue(commentFromDB.isPresent());

        Reply reply = new Reply(userFromDB.getUser_id(),commentFromDB.get().getCommendID(),"ok",new Date(),false,false,userFromDB.getUser_id());
        replyRepository.save(reply);
        List<Reply> replyFromDB = replyRepository.findAllByCommentID(commentFromDB.get().getCommendID());
        assertEquals(replyFromDB.size(),1);

        commentService.deleteReply(replyFromDB.get(0).getReplyID(),"newAdmin");

        List<Reply> replyFromDB2 = replyRepository.findAllByCommentID(commentFromDB.get().getCommendID());
        assertEquals(replyFromDB2.size(),0);
    }

}