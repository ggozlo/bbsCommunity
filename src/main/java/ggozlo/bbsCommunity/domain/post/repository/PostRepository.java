package ggozlo.bbsCommunity.domain.post.repository;

import ggozlo.bbsCommunity.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface PostRepository extends JpaRepository<Post, Long>, PostJpaRepository{



    @Transactional(readOnly = true)
    @Query("select p.member.username from Post p inner join p.member m where p.id = :postId")
    String findAuthor(@Param("postId") Long postId);

}
