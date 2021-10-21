package ggozlo.bbsCommunity.global.security.detailsService;

import ggozlo.bbsCommunity.domain.member.Member;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
public class SessionMember extends User {

    private Member member;

    public SessionMember(Member member, Collection<? extends GrantedAuthority> authorities) {
        super(member.getUsername(), member.getPassword(), authorities);
        this.member = member;
    }
}
