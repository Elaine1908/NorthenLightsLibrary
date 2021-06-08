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
public class CommentRequest {
    @NotNull(message = "isbn不能为空")
    private String isbn;

    @NotNull(message = "评论的内容不能为空")
    private String content;

    ///^([0-9]|10)$/
    @NotNull(message = "打分不能为空")
    private long rate;
}
