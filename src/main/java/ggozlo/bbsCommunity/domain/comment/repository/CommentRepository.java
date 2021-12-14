package ggozlo.bbsCommunity.domain.comment.repository;

import ggozlo.bbsCommunity.domain.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CommentRepository  extends JpaRepository<Comment, Long> , CommentJpaRepository{

    @Transactional(readOnly = true)
    @Query("select c from Comment c join fetch c.member where c.post.id = :postId")
    List<Comment> findByPostId(@Param("postId") Long postId);
}
