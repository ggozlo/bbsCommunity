package ggozlo.bbsCommunity.global.dto;

import lombok.Data;

@Data
public class MemberEditDto {

    private String loginId;
    private String password;
    private String email;
    private String name;
}
