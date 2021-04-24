package com.example.lab2.request.upload;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


/**
 * 添加书本的副本的请求
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Valid
public class AddBookCopyRequest {

    @NotNull(message = "isbn不能为空")
    private String isbn;

    @NotNull(message = "图书馆id不能为空")
    @Pattern(regexp = "[0-9]+", message = "图书馆id必须为数字")
    private String libraryID;

    @NotNull(message = "添加的书本数量不能为空")
    @Pattern(regexp = "[0-9]+", message = "添加书的数目必须为数字")
    private String number;


}
