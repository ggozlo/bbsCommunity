package ggozlo.bbsCommunity.domain.member;

import ggozlo.bbsCommunity.domain.member.repository.MemberRepository;
import ggozlo.bbsCommunity.domain.member.service.MemberService;
import ggozlo.bbsCommunity.global.exception.member.AlreadyExistMemberException;
import ggozlo.bbsCommunity.global.exception.member.DeleteFailureException;
import ggozlo.bbsCommunity.global.exception.member.JoinFailureException;
import ggozlo.bbsCommunity.global.exception.member.NotFoundMemberException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    private Member testMember;
    private List<Member> memberList = new ArrayList<>();

    @BeforeEach
    void init() {
        testMember = new Member(testUsername, testPassword, testEmail, testNickname);
        memberList.add(memberService.join(testMember));
        for (int i = 1; i < testUserCount; i++) {
            Member testMember = new Member();
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
    }


    @Test
    @DisplayName("회원 생성 결과 확인")
    void joinTest() {
        List<Member> all = memberRepository.findByUsernameContaining(testUsername);
        assertEquals(all.size(), testUserCount);
        assertIterableEquals(all, memberList);
        assertThrows(JoinFailureException.class,
                () -> memberService.join((new Member(testUsername, testPassword, testEmail, testNickname))));
    }
    
    @Test
    @DisplayName("회원 수정 테스트")
    void editTest() {

        String changeUsername = "changeUsername";
        String changePassword = "changePassword";
        String changeEmail = "changeEmail";
        String changeNickname = "changeNickname";

        assertThrows(AlreadyExistMemberException.class,
                () -> memberService.changeUsername(testMember.getId(), testUsername));
        assertThrows(AlreadyExistMemberException.class,
                () -> memberService.changeEmail(testMember.getId(), testEmail));
        assertThrows(AlreadyExistMemberException.class,
                () -> memberService.changeNickname(testMember.getId(), testNickname));

        memberService.changeUsername(testMember.getId(), changeUsername);
        memberService.changePassword(testMember.getId(), changePassword);
        memberService.changeEmail(testMember.getId(), changeEmail);
        memberService.changeNickname(testMember.getId(), changeNickname);

        Member changeMember = memberService.findById(testMember.getId());
        assertEquals(changeMember.getUsername(), changeUsername);
        assertTrue(passwordEncoder.matches(changePassword, changeMember.getPassword()));
        assertEquals(changeMember.getEmail(), changeEmail);
        assertEquals(changeMember.getNickname(), changeNickname);

    }

    @Test
    @DisplayName("멤버 삭제 테스트")
    void deleteTest() {
        memberService.deleteMember(testMember.getId());
        assertThrows(NotFoundMemberException.class,
                () -> memberService.findById(testMember.getId()));
    }



}