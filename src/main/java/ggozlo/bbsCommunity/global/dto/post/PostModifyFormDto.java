package ggozlo.bbsCommunity.global.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostModifyFormDto {
    private String title;
    private String content;
}
