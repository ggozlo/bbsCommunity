package ggozlo.bbsCommunity.global.dto.board;

import com.querydsl.core.annotations.QueryProjection;
import ggozlo.bbsCommunity.domain.board.Board;
import ggozlo.bbsCommunity.global.dto.post.PostListDto;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

@Data
public class BoardMainDto {

    private String boardName;
    private String describe;
    private Page<PostListDto> postPage;
    private boolean activation;

    private LocalDateTime createDate;
    private LocalDateTime lastModifiedDate;

    public BoardMainDto(Board board) {
        this.boardName = board.getName();
        this.describe = board.getDescribe();
        board.getPostList().stream().map(PostListDto::new).forEach(postListDto -> postPage.getContent().add(postListDto));
    }

    @QueryProjection
    public BoardMainDto(String boardName, String describe, boolean activation, LocalDateTime createDate, LocalDateTime lastModifiedDate) {
        this.boardName = boardName;
        this.describe = describe;
        this.activation = activation;
        this.createDate = createDate;
        this.lastModifiedDate = lastModifiedDate;
    }
}
