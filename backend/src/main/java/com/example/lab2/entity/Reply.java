package com.example.lab2.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;


/**
 * 数据库中，存储用户在某人的评论下面的回复的实体类
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long replyID;

    private long userID;

    private long commentID;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(columnDefinition = "text")
    private String content;

    private Date time;

    private boolean deletedByAdmin;

    private boolean deletedBySelf;


    //被回复的用户id，即谁被回复了。
    private long repliedUserID;

    public Reply(long userID, long commentID, String content, Date time, boolean deletedByAdmin, boolean deletedBySelf, long repliedUserID) {
        this.userID = userID;
        this.commentID = commentID;
        this.content = content;
        this.time = time;
        this.deletedByAdmin = deletedByAdmin;
        this.deletedBySelf = deletedBySelf;
        this.repliedUserID = repliedUserID;
    }
}
