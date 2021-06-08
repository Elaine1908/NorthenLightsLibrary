package com.example.lab2.dao;

import com.example.lab2.dto.commentreply.CommentDTO;
import com.example.lab2.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * 根据isbn从数据库中得到这种booktype下面的全部的评论。注意这里的CommentDTO里面不含有List<ReplyDTO>，需要另外注入
     *
     * @param isbn
     * @return
     */
    @Query("select new com.example.lab2.dto.commentreply.CommentDTO(cm.commendID,ur.username,cm.content,cm.rate,cm.time,cm.deletedByAdmin)" +
            "from Comment cm left join User ur on cm.userID=ur.user_id where cm.isbn=:isbn")
    public List<CommentDTO> getCommentsByIsbn(@Param("isbn") String isbn);


    public default Double getAverageRateByISBN(@Param("isbn") String isbn) {
        List<CommentDTO> commentDTOList = this.getCommentsByIsbn(isbn);
        if (commentDTOList.size() == 0) {
            return 0.0;
        }
        double sum = commentDTOList.stream().mapToDouble(commentDTO -> (double) commentDTO.getRate()).sum();
        return sum / commentDTOList.size();
    }

}
