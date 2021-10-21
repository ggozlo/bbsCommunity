package ggozlo.bbsCommunity.global.dto;

import lombok.Data;

import javax.persistence.Column;

@Data
public class MemberInfoDto {

    private String username;
    private String email;
    private String nickname;

}
