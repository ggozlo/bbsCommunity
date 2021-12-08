package ggozlo.bbsCommunity.global.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Lob;

@Data
@AllArgsConstructor
public class PostWriteDto {

    private String title;
    private String content;
    private Long authorId;
    private String boardAddress;
    private Integer views;
}
