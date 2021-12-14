package ggozlo.bbsCommunity.global.dto.member;

import com.querydsl.core.annotations.QueryProjection;
import ggozlo.bbsCommunity.domain.post.Post;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MemberPostList {

    private Long postId;
    private String title;
    private String authorNickname;
    private String boardAddress;
    private String boardName;
    private int views;
    private LocalDateTime lastModifiedTime;

    public MemberPostList(Post post) {
        this.postId = post.getId();
        this.title = post.getTitle();
        this.authorNickname = post.getMember().getNickname();
        this.views = post.getViews();
    }

    @QueryProjection

    public MemberPostList(Long postId, String title, String authorNickname, String boardAddress, String boardName, int views, LocalDateTime lastModifiedTime) {
        this.postId = postId;
        this.title = title;
        this.authorNickname = authorNickname;
        this.boardAddress = boardAddress;
        this.boardName = boardName;
        this.views = views;
        this.lastModifiedTime = lastModifiedTime;
    }
}
