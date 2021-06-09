package com.example.lab2.request.comment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@Valid
public class ReplyRequest {
    @NotNull(message = "评论的内容不能为空")
    private String content;

    private Long commentID;

    private Long replyID;
}
