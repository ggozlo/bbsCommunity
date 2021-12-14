package ggozlo.bbsCommunity.global.dto.comment;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentListDto {

    private Long postId;
    private String postTitle;
    private String boardAddress;
    private String content;
    private LocalDateTime createDate;



    @QueryProjection
    public CommentListDto(Long postId, String postTitle, String boardAddress, String content, LocalDateTime createDate) {
        this.postId = postId;
        this.postTitle = postTitle;
        this.boardAddress = boardAddress;
        this.content = content;
        this.createDate = createDate;
    }
}
