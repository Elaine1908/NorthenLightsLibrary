package com.example.lab2.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

/**
 * 用户在某种BookType下的评论，在数据库中的实体类
 */
@Entity
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long commendID;

    private long userID;

    private String isbn;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(columnDefinition = "text")
    private String content;

    private Date time;

    private boolean deletedBySelf;

    private boolean deletedByAdmin;

    private long rate;

    public Comment(long userID, String isbn, String content, Date time, boolean deletedBySelf, boolean deletedByAdmin, long rate) {
        this.userID = userID;
        this.isbn = isbn;
        this.content = content;
        this.time = time;
        this.deletedBySelf = deletedBySelf;
        this.deletedByAdmin = deletedByAdmin;
        this.rate = rate;
    }
}
