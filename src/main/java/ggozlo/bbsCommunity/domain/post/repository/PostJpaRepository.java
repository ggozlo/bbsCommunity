package ggozlo.bbsCommunity.domain.post.repository;

import ggozlo.bbsCommunity.domain.post.Post;

import java.util.Optional;

public interface PostJpaRepository {

    Long persistPost(Post post);

    Optional<Post> viewPost(Long postId);

    void incrementViews(Long postId);

//    String findAuthor(Long postId);
}
