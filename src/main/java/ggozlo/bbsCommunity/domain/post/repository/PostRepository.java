package ggozlo.bbsCommunity.domain.post.repository;

import ggozlo.bbsCommunity.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
}
