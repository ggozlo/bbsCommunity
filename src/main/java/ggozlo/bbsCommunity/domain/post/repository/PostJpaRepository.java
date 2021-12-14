package ggozlo.bbsCommunity.domain.post.repository;

import ggozlo.bbsCommunity.domain.post.Post;
import ggozlo.bbsCommunity.global.dto.member.MemberPostList;
import ggozlo.bbsCommunity.global.dto.post.PostModifyFormDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PostJpaRepository {

    Long persistPost(Post post);

    Optional<Post> viewPost(Long postId);

    void incrementViews(Long postId);

    void updatePost(Long postId, PostModifyFormDto modifyPost);

    Optional<PostModifyFormDto> findModifyPost(Long postId);

    Page<MemberPostList> memberPostPage(Long memberId, Pageable pageable);


//    String findAuthor(Long postId);
}
