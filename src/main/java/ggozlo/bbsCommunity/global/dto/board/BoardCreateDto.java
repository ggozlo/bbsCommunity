package ggozlo.bbsCommunity.global.dto.board;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class BoardCreateDto {

    private String address;
    private String name;
    private String describe;
    private Long applicantId;
}
