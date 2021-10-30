package ggozlo.bbsCommunity.domain.member.contoller;

import ggozlo.bbsCommunity.domain.member.Member;
import ggozlo.bbsCommunity.global.dto.member.MemberInfoDto;
import ggozlo.bbsCommunity.global.dto.member.MemberJoinDto;
import ggozlo.bbsCommunity.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    // permit All
    // 회원가입 form 화면 메서드
    @GetMapping("/join")
    public String joinForm(@ModelAttribute("memberJoinForm") MemberJoinDto memberDto) {
        return "member/joinForm";
    }

    // permit All
    // 회원가입 처리 메서드
    @PostMapping("/join")
    public String join(MemberJoinDto joinDto, Model model) {
        log.debug("Member Join Request : {}", joinDto);
        memberService.join(joinDto);
        return "home";
    }

    // 로그인한 회원 본인만 접근가능
    // 회원정보 출력 메서드
    @GetMapping("/info")
    @PreAuthorize("isAuthenticated()")
    public String inquireMember(@AuthenticationPrincipal Member member, Model model) {
        log.debug("Inquire Member ID:{}", member.getId());
        MemberInfoDto memberInfoDto =  memberService.findById(member.getId());
        model.addAttribute("member", memberInfoDto);
        return "member/detail";
    }

    // 계정 삭제 메소드
    @PostMapping("/delete")
    @PreAuthorize("isAuthenticated()")
    public String deleteMember(@AuthenticationPrincipal Member member) {
        log.debug("Delete Member Request Id: {} , Username: {}", member.getId(), member.getUsername());
        memberService.deleteMember(member.getId());
        return "redirect:/logout";
    }


    // 로그인 회원 본인만 접근가능한 사용자정보 변경 메서드들
    @PostMapping("/edit/username")
    @PreAuthorize("isAuthenticated()")
    public String editUsername(@AuthenticationPrincipal Member member, String newUsername, Model model) {
        log.debug("Edit Username Request {} to {}", member.getUsername(), newUsername);
        memberService.changeUsername(member.getId(), newUsername);
        member.setUsername(newUsername);
        return "redirect:/member/info";
    }

    @PostMapping("/edit/password")
    @PreAuthorize("isAuthenticated()")
    public String editPassword(@AuthenticationPrincipal Member member, String newPassword, Model model) {
        log.debug("Edit Password Request {} to {}", member.getPassword(), newPassword);
        memberService.changePassword(member.getId(), newPassword);
        member.setPassword(newPassword);
        return "redirect:/member/info";
    }

    @PostMapping("/edit/email")
    @PreAuthorize("isAuthenticated()")
    public String editEmail(@AuthenticationPrincipal Member member, String newEmail, Model model) {
        log.debug("Edit Email Request {} to {}", member.getEmail(), newEmail);
        memberService.changeEmail(member.getId(), newEmail);
        member.setEmail(newEmail);
        return "redirect:/member/info";
    }

    @PostMapping("/edit/nickname")
    @PreAuthorize("isAuthenticated()")
    public String editNickname(@AuthenticationPrincipal Member member, String newNickname, Model model) {
        log.debug("Edit Nickname Request {} to {}", member.getNickname(), newNickname);
        memberService.changeNickname(member.getId(), newNickname);
        member.setNickname(newNickname);
        return "redirect:/member/info";
    }


}
