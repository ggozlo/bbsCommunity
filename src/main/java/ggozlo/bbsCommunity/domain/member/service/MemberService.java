package ggozlo.bbsCommunity.domain.member.service;

import ggozlo.bbsCommunity.domain.member.authority.Authority;
import ggozlo.bbsCommunity.domain.member.Member;
import ggozlo.bbsCommunity.domain.member.authority.AuthorityRepository;
import ggozlo.bbsCommunity.domain.member.repository.MemberRepository;
import ggozlo.bbsCommunity.global.exception.member.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원가입 서비스메서드
    @Transactional
    public Member join(Member member) {
        uniqueCheck(member);
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        memberRepository.persistMember(member);
        authorityRepository.save(new Authority(member, null, member.getUsername()));
        return member;
    }


    // 개별 회원 검색 메서드, 없다면 예외발생
    public Member findById(Long memberId) {
        Optional<Member> findMember = memberRepository.findById(memberId);
        Member member = findMember.orElseThrow(() -> new NotFoundMemberException("Ex.Member.NotFoundMember"));
        return member;
    }

    // 사용자 수정 메소드
    // 계정이 존재하는걸 전제로 작동하는 메서드
    @Transactional
    public void changeUsername(Long memberId, String newUsername) {
        checkUsername(newUsername);
        memberRepository.updateUsername(memberId, newUsername);
    }
    @Transactional
    public void changePassword(Long memberId, String newPassword) {
        memberRepository.updatePassword(memberId, passwordEncoder.encode(newPassword));
    }
    @Transactional
    public void changeEmail(Long memberId, String newEmail) {
        checkEmail(newEmail);
        memberRepository.updateEmail(memberId, newEmail);
    }
    @Transactional
    public void changeNickname(Long memberId, String newNickname) {
        checkNickname(newNickname);
        memberRepository.updateNickname(memberId, newNickname);
    }

    // 계정 삭제 머소드
    @Transactional
    public void deleteMember(Long memberId) {
        memberRepository.deleteById(memberId);
        boolean exist = memberRepository.existsById(memberId);
        if (exist) {
            throw new DeleteFailureException("Ex.Member.DeleteFail");
        }
    }

    /**
     *  사용자 계정생성 전에 유일성 조건이 일치하는지 확인하는 개별메서드들
     *  조회만 하므로 트랜잭션 적용 안함
     */

    private void uniqueCheck(Member member) {
        boolean check = memberRepository
                .existsByUsernameOrEmailOrNickname(member.getUsername(), member.getEmail(), member.getNickname());
        if (check) {
            throw new JoinFailureException("Ex.Member.JoinFail");
        }
    }
    private void checkNickname(String nickname) {
        boolean byName = memberRepository.existsByNickname(nickname);
        if (byName) {
            log.info("already exist nickname = {}",nickname);
            throw new AlreadyExistMemberException("Ex.Member.ExistNickname");
        }
    }
    private void checkEmail(String email) {
        boolean byEmail = memberRepository.existsByEmail(email);
        if (byEmail) {
            log.info("already exist email = {}", email);
            throw new AlreadyExistMemberException("Ex.Member.ExistEmail");

        }
    }

    private void checkUsername(String username) {
        boolean byLoginId = memberRepository.existsByUsername(username);
        if (byLoginId) {
            log.info("already exist username = {}", username);
            throw new AlreadyExistMemberException("Ex.Member.ExistUsername");
        }
    }

}
