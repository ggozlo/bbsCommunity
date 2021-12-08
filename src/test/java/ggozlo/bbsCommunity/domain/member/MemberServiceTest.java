package ggozlo.bbsCommunity.domain.member;

import ggozlo.bbsCommunity.domain.member.repository.MemberRepository;
import ggozlo.bbsCommunity.domain.member.service.MemberService;
import ggozlo.bbsCommunity.global.dto.member.MemberInfoDto;
import ggozlo.bbsCommunity.global.dto.member.MemberJoinDto;
import ggozlo.bbsCommunity.global.exception.member.AlreadyExistMemberException;
import ggozlo.bbsCommunity.global.exception.member.JoinFailureException;
import ggozlo.bbsCommunity.global.exception.member.NotFoundMemberException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    private final int testUserCount = 3;
    private final String testUsername = "testUsername";
    private final String testPassword = "test1234";
    private final String testEmail = "test@Email.com";
    private final String testNickname = "testNickname";
    private MemberJoinDto testMember;
    private Long testMemberId;
    private List<MemberJoinDto> memberList = new ArrayList<>();

    @BeforeEach
    void init() {
        testMember = new MemberJoinDto(testUsername, testPassword, testEmail, testNickname);
        testMemberId = memberService.join(testMember).getId();
        memberList.add(testMember);
        for (int i = 1; i < testUserCount; i++) {
            MemberJoinDto testMember = new MemberJoinDto();
            testMember.setUsername(testUsername + i);
            testMember.setPassword(testPassword);
            testMember.setEmail(i+testEmail);
            testMember.setNickname(testNickname + i);
            memberService.join(testMember);
            memberList.add(testMember);
        }
    }

    @AfterEach
    void clean() {
        memberRepository.deleteAll();
        memberList.clear();
    }


    @Test
    @DisplayName("회원 생성 결과 확인")
    void joinTest() {
        List<Member> all = memberRepository.findByUsernameContaining(testUsername);
        assertEquals(all.size(), testUserCount);
        assertThrows(JoinFailureException.class,
                () -> memberService.join((new MemberJoinDto(testUsername, testPassword, testEmail, testNickname))));
    }
    
    @Test
    @DisplayName("회원 수정 테스트")
    void editTest() {

        String changeUsername = "changeUsername";
        String changePassword = "changePassword";
        String changeEmail = "changeEmail";
        String changeNickname = "changeNickname";

        assertThrows(AlreadyExistMemberException.class,
                () -> memberService.changeUsername(testMemberId, testUsername+1));
        assertThrows(AlreadyExistMemberException.class,
                () -> memberService.changeEmail(testMemberId, 1+testEmail));
        assertThrows(AlreadyExistMemberException.class,
                () -> memberService.changeNickname(testMemberId, testNickname+1));

        memberService.changeUsername(testMemberId, changeUsername);
        memberService.changePassword(testMemberId, changePassword);
        memberService.changeEmail(testMemberId, changeEmail);
        memberService.changeNickname(testMemberId, changeNickname);

        Member changeMember = memberRepository.findById(testMemberId).get();
        assertEquals(changeMember.getUsername(), changeUsername);
        assertTrue(passwordEncoder.matches(changePassword, changeMember.getPassword()));
        assertEquals(changeMember.getEmail(), changeEmail);
        assertEquals(changeMember.getNickname(), changeNickname);

    }

    @Test
    @DisplayName("멤버 삭제 테스트")
    void deleteTest() {
        memberService.deleteMember(testMemberId);
        assertThrows(NotFoundMemberException.class, () -> memberService.findById(testMemberId));
    }



}