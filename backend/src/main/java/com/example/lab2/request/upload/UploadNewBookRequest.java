package com.example.lab2.request.upload;

import com.example.lab2.entity.BookType;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Valid
@Data
public class UploadNewBookRequest {
    @NotNull(message = "文件不能为空")
    private MultipartFile bookcoverimage;

    @NotNull(message = "isbn不能为空")
    private String isbn;
    @NotNull(message = "书名不能为空")
    private String name;
    @NotNull(message = "书本的作者不能为空")
    private String author;
    @NotNull(message = "书本的描述不能为空")
    private String description;
    @NotNull(message = "出版日期不能为空")
    @Pattern(regexp = "((((19|20)\\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\\d|3[01]))|(((19|20)\\d{2})-(0?[469]|11)-(0?[1-9]|[12]\\d|30))|(((19|20)\\d{2})-0?2-(0?[1-9]|1\\d|2[0-8]))|((((19|20)([13579][26]|[2468][048]|0[48]))|(2000))-0?2-(0?[1-9]|[12]\\d)))",
            message = "出版日期必须符合规范！")
    private String publicationDate;


    /**
     * 根据request中的信息，生成一个book对象
     *
     * @return book对象
     */
    public BookType getBook() {
        BookType b = new BookType();
        b.setIsbn(isbn);
        b.setName(name);
        b.setAuthor(author);
        b.setDescription(description);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+:08:00"));
        try {
            b.setPublicationDate(formatter.parse(this.publicationDate));
        } catch (ParseException parseException) {
            b.setPublicationDate(null);
        }

        return b;
    }


}
