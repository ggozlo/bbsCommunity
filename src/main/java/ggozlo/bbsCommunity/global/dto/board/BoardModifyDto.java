package ggozlo.bbsCommunity.global.dto.board;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class BoardModifyDto {

    private String name;

    private String describe;

    @QueryProjection
    public BoardModifyDto(String name, String describe) {
        this.name = name;
        this.describe = describe;
    }
}
