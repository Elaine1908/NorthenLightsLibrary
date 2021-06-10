package com.example.lab2.dao;

import com.example.lab2.dto.commentreply.ReplyDTO;
import com.example.lab2.entity.Library;
import com.example.lab2.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {

    @Query("select new com.example.lab2.dto.commentreply.ReplyDTO(rp.replyID,ur.username,rp.content,rp.time,rp.deletedByAdmin,repliedUr.username)" +
            "from Reply rp left join User ur on ur.user_id=rp.userID left join User repliedUr on repliedUr.user_id=rp.repliedUserID where rp.commentID=:commentID")
    public List<ReplyDTO> getRepliesByCommentID(@Param("commentID") long commentID);

    public void deleteAllByCommentID(Long commentID);

    public List<Reply> findAllByCommentID(Long commentID);

    @Query("select r from Reply r where r.commentID=:commentID")
    public List<Reply> getAllByCommentID(@Param("commentID") Long commentID);

}
