package ggozlo.bbsCommunity.global.security.detailsService;

import ggozlo.bbsCommunity.domain.member.Member;
import ggozlo.bbsCommunity.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Member> loginMember = memberRepository.findByUsername(username);

        if (loginMember.isEmpty()) {
            if (memberRepository.countByUsername(username) == 0) {
                throw new UsernameNotFoundException("Security.Ex.UsernameNotFound");
            }
        }

        Member member = loginMember.get();
        List<GrantedAuthority> authorityList = member
                .getAuthorityList()
                .stream()
                .map(authority -> new SimpleGrantedAuthority("ROLE_"+authority.getRole()))
                .collect(Collectors.toList());

        return new SessionMember(member, authorityList);
    }
}
