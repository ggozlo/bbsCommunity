package ggozlo.bbsCommunity.global;

import ggozlo.bbsCommunity.domain.member.Member;
import ggozlo.bbsCommunity.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class Initializer {

    private final MemberService memberService;

    @EventListener(ApplicationReadyEvent.class)
    public void init(){
        Member admin = new Member("admin", "1234", "ggozlo@naver.com", "admInUser");
        memberService.join(admin);
        log.debug("Success Initialize!");
    }
}
