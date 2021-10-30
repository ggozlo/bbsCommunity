package ggozlo.bbsCommunity.global.dto.post;

import ggozlo.bbsCommunity.domain.post.Post;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostViewDto {

    private String title;
    private String authorNickname;
    private String boardNickname;
    private String content;

    private LocalDateTime createDate;
    private LocalDateTime lastModifiedDate;

    private Integer views;

    private boolean isAuthor;

    public  PostViewDto(Post post) {
        this.title = post.getTitle();
        this.authorNickname = post.getMember().getNickname();
        this.boardNickname = post.getBoard().getName();
        this.content = post.getContent();
        this.createDate = post.getCreateDate();
        this.lastModifiedDate = post.getLastModifiedDate();
        this.views = post.getViews();
        this.isAuthor = false;
    }


}
