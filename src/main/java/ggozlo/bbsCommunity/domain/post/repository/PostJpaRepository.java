package ggozlo.bbsCommunity.domain.post.repository;

import ggozlo.bbsCommunity.domain.post.Post;
import ggozlo.bbsCommunity.global.dto.post.PostModifyFormDto;

import java.util.Optional;

public interface PostJpaRepository {

    Long persistPost(Post post);

    Optional<Post> viewPost(Long postId);

    void incrementViews(Long postId);

    void updatePost(Long postId, PostModifyFormDto modifyPost);

    Optional<PostModifyFormDto> findModifyPost(Long postId);

//    String findAuthor(Long postId);
}
