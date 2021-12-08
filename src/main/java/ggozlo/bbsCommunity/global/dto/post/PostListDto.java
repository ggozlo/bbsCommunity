package ggozlo.bbsCommunity.global.dto.post;

import com.querydsl.core.annotations.QueryProjection;
import ggozlo.bbsCommunity.domain.post.Post;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostListDto {

    private Long postId;
    private String title;
    private String authorNickname;
    private int views;
    private LocalDateTime lastModifiedTime;

    public PostListDto(Post post) {
        this.postId = post.getId();
        this.title = post.getTitle();
        this.authorNickname = post.getMember().getNickname();
        this.views = post.getViews();
    }

    @QueryProjection
    public PostListDto(Long postId, String title, String authorNickname, int views, LocalDateTime lastModifiedTime) {
        this.postId = postId;
        this.title = title;
        this.authorNickname = authorNickname;
        this.views = views;
        this.lastModifiedTime = lastModifiedTime;
    }
}
