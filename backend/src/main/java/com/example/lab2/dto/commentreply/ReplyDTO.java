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

    public ReplyDTO(long replyID, String username, String content, Date time, boolean deletedByAdmin) {
        this.replyID = replyID;
        this.username = username;
        this.content = content;
        this.time = time;
        this.deletedByAdmin = deletedByAdmin;
    }
}
