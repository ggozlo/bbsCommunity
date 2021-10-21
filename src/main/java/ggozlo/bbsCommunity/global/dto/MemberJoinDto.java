package ggozlo.bbsCommunity.global.dto;

import lombok.Data;


@Data
public class MemberJoinDto {

    private String username;
    private String password;
    private String email;
    private String nickname;

}
