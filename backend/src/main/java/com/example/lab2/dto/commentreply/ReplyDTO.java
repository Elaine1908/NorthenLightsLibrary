package com.example.lab2.dto.commentreply;

import lombok.*;

import javax.persistence.Access;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class ReplyDTO {

    private long replyID;

    private String username;

    private String content;

    private Date time;

    private boolean deletedByAdmin;

    private String repliedUsername;

    public ReplyDTO(long replyID, String username, String content, Date time, boolean deletedByAdmin, String repliedUsername) {
        this.replyID = replyID;
        this.username = username;
        this.content = content;
        this.time = time;
        this.deletedByAdmin = deletedByAdmin;
        this.repliedUsername = repliedUsername;
    }
}
