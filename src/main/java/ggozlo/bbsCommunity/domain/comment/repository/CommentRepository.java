package ggozlo.bbsCommunity.domain.comment.repository;

import ggozlo.bbsCommunity.domain.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository  extends JpaRepository<Comment, Long> {
}
