package ggozlo.bbsCommunity.global.security.provider;

import ggozlo.bbsCommunity.global.security.detailsService.SessionMember;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@RequiredArgsConstructor
public class FormAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        SessionMember sessionMember = (SessionMember) userDetailsService.loadUserByUsername(username);

        if (!passwordEncoder.matches(password, sessionMember.getPassword())) {
            throw new BadCredentialsException("Ex.Sec.PasswordNotMatch");
        }
        log.info("login member: {}", sessionMember.getMember().getUsername());
        return new UsernamePasswordAuthenticationToken(sessionMember.getMember(), null, sessionMember.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
