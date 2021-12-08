package ggozlo.bbsCommunity.global.dto.post;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class PostModifyFormDto {
    private String boardName;
    private String title;
    private String content;

    public PostModifyFormDto() {
    }

    @QueryProjection
    public PostModifyFormDto(String boardName, String title, String content) {
        this.boardName = boardName;
        this.title = title;
        this.content = content;
    }
}
