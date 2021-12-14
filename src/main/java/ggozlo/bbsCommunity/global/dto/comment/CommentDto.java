package ggozlo.bbsCommunity.global.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDto {

    private Long commentId;
    private String authorNickname;
    private String comment;
    private boolean author;


    public CommentDto(Long commentId, String authorNickname, String comment) {
        this.commentId = commentId;
        this.authorNickname = authorNickname;
        this.comment = comment;
        this.author = false;
    }
}
