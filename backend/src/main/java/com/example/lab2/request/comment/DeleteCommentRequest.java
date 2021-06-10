package com.example.lab2.request.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Valid
public class DeleteCommentRequest {
    @NotNull(message = "删除的commentID不能为空")
    private Long commentID;
}
