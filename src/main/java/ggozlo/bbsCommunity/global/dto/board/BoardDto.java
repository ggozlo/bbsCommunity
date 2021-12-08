package ggozlo.bbsCommunity.global.dto.board;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class BoardDto {

    private String address;

    private String name;

    private String describe;

    private String primeManager;

    private LocalDateTime createDate;

    public BoardDto(String address, String name, String describe, String primeManager, LocalDateTime createDate) {
        this.address = address;
        this.name = name;
        this.describe = describe;
        this.primeManager = primeManager;
        this.createDate = createDate;
    }
}
