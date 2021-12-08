package ggozlo.bbsCommunity.global.dto.board;

import com.querydsl.core.annotations.QueryProjection;
import ggozlo.bbsCommunity.domain.board.Board;
import ggozlo.bbsCommunity.global.dto.post.PostListDto;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
public class BoardMainDto {

    private String boardName;
    private String describe;
    private Page<PostListDto> postPage;

    public BoardMainDto(Board board) {
        this.boardName = board.getName();
        this.describe = board.getDescribe();
        board.getPostList().stream().map(PostListDto::new).forEach(postListDto -> postPage.getContent().add(postListDto));
    }

    @QueryProjection
    public BoardMainDto(String boardName, String describe) {
        this.boardName = boardName;
        this.describe = describe;
    }
}