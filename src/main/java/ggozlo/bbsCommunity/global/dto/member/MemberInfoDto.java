package ggozlo.bbsCommunity.global.dto.member;

import lombok.Data;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Data
public class MemberInfoDto {

    private Long id;
    private String username;
    private String email;
    private String nickname;

    private LocalDateTime createDate;

    private LocalDateTime lastModifiedDate;

}
