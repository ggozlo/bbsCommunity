package ggozlo.bbsCommunity.domain.member.service;

import ggozlo.bbsCommunity.domain.member.authority.Authority;
import ggozlo.bbsCommunity.domain.member.Member;
import ggozlo.bbsCommunity.domain.member.authority.AuthorityRepository;
import ggozlo.bbsCommunity.domain.member.repository.MemberRepository;
import ggozlo.bbsCommunity.global.dto.member.MemberInfoDto;
import ggozlo.bbsCommunity.global.dto.member.MemberJoinDto;
import ggozlo.bbsCommunity.global.exception.member.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    // 회원가입 서비스메서드
    public Long join(MemberJoinDto memberDto) {
        try {
            Member member = modelMapper.map(memberDto, Member.class);
            member.setPassword(passwordEncoder.encode(member.getPassword()));
            memberRepository.persistMember(member);
            return member.getId();
        } catch (DataIntegrityViolationException e) {
            e.getMessage();
            throw new JoinFailureException("Ex.Member.JoinFail");
        } catch (Exception e) {
            e.printStackTrace();
            throw new MemberException("Ex.Member");
        }
    }


    // 개별 회원 검색 메서드, 없다면 예외발생
    @Transactional(readOnly = true)
    public MemberInfoDto findById(Long memberId) {
            Optional<Member> findMember = memberRepository.findById(memberId);
            Member member = findMember.orElseThrow(() -> new NotFoundMemberException("Ex.Member.NotFound"));
            return modelMapper.map(member, MemberInfoDto.class);
    }

    // 사용자 수정 메소드
    // 계정이 존재하는걸 전제로 작동하는 메서드
    public void changeUsername(Long memberId, String newUsername) {
        try {
            memberRepository.updateUsername(memberId, newUsername);
        } catch (DataIntegrityViolationException e) {
            e.getMessage();
            throw new AlreadyExistMemberException("Ex.Member.ExistUsername");
        } catch (Exception e) {
            e.printStackTrace();
            throw new MemberException("Ex.Member");
        }
    }

    public void changePassword(Long memberId, String newPassword) {
        memberRepository.updatePassword(memberId, passwordEncoder.encode(newPassword));
    }

    public void changeEmail(Long memberId, String newEmail) {
        try {
            memberRepository.updateEmail(memberId, newEmail);
        } catch (DataIntegrityViolationException e) {
            e.getMessage();
            throw new AlreadyExistMemberException("Ex.Member.ExistEmail");
        } catch (Exception e) {
            e.printStackTrace();
            throw new MemberException("Ex.Member");
        }
    }

    public void changeNickname(Long memberId, String newNickname) {
        try {
            memberRepository.updateNickname(memberId, newNickname);
        } catch (DataIntegrityViolationException e) {
            e.getMessage();
            throw new AlreadyExistMemberException("Ex.Member.ExistNickname");
        } catch (Exception e) {
            e.printStackTrace();
            throw new MemberException("Ex.Member");
        }
    }

    // 계정 삭제 머소드
    public void deleteMember(Long memberId) {
            memberRepository.deleteById(memberId);
            boolean exist = memberRepository.existsById(memberId);
            if (exist) {
                throw new DeleteFailureException("Ex.Member.DeleteFail");
            }

    }
}
