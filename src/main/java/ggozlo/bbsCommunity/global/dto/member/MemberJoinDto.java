package ggozlo.bbsCommunity.global.dto.member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberJoinDto {

    private String username;
    private String password;
    private String email;
    private String nickname;

}
