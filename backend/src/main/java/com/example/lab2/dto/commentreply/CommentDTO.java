package com.example.lab2.dto.commentreply;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class CommentDTO {

    private long commentID;

    private String username;

    private String content;

    private long rate;

    private Date time;

    private boolean deletedByAdmin;

    private List<ReplyDTO> replyList;

    public CommentDTO(long commentID, String username, String content, long rate, Date time, boolean deletedByAdmin, List<ReplyDTO> replyList) {
        this.commentID = commentID;
        this.username = username;
        this.content = content;
        this.rate = rate;
        this.time = time;
        this.deletedByAdmin = deletedByAdmin;
        this.replyList = replyList;
    }

    public CommentDTO(long commentID, String username, String content, long rate, Date time, boolean deletedByAdmin) {
        this.commentID = commentID;
        this.username = username;
        this.content = content;
        this.rate = rate;
        this.time = time;
        this.deletedByAdmin = deletedByAdmin;
    }
}
