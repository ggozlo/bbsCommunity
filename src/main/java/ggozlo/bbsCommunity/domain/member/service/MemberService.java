package ggozlo.bbsCommunity.domain.member.service;

import ggozlo.bbsCommunity.domain.comment.repository.CommentRepository;
import ggozlo.bbsCommunity.domain.member.authority.Authority;
import ggozlo.bbsCommunity.domain.member.Member;
import ggozlo.bbsCommunity.domain.member.authority.AuthorityRepository;
import ggozlo.bbsCommunity.domain.member.repository.MemberRepository;
import ggozlo.bbsCommunity.domain.post.repository.PostRepository;
import ggozlo.bbsCommunity.global.dto.comment.CommentListDto;
import ggozlo.bbsCommunity.global.dto.member.MemberInfoDto;
import ggozlo.bbsCommunity.global.dto.member.MemberJoinDto;
import ggozlo.bbsCommunity.global.dto.member.MemberPostList;
import ggozlo.bbsCommunity.global.dto.post.PostListDto;
import ggozlo.bbsCommunity.global.exception.member.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final AuthorityRepository authorityRepository;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public Long createAdmin(String username, String password, String email, String nickName) {

        Member admin = new Member(username, passwordEncoder.encode(password), email, nickName);
        memberRepository.persistMember(admin);
        Authority adminRole = new Authority(admin, null, username);
        authorityRepository.persistAuthority(adminRole);
        return admin.getId();

    }

    // 회원가입 서비스메서드
    public Member join(MemberJoinDto memberDto) {
        try {
            Member member = modelMapper.map(memberDto, Member.class);
            member.setPassword(passwordEncoder.encode(member.getPassword()));
            Member joinMember = memberRepository.persistMember(member);
            authorityRepository.persistAuthority(new Authority(joinMember, null, joinMember.getUsername()));
            return joinMember;
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


    public Page<MemberPostList> findPostList(Long memberId, Pageable pageable) {
        Page<MemberPostList> postList = postRepository.memberPostPage(memberId, pageable);
        return postList;
    }

    public Page<CommentListDto> findCommentList(Long memberId, Pageable pageable) {
        Page<CommentListDto> commentPage = commentRepository.memberCommentPage(memberId, pageable);
        return commentPage;
    }

    public List<MemberInfoDto> memberList() {
        List<Member> memberList = memberRepository.findAll();
        List<MemberInfoDto> memberDtoList = memberList.stream()
                .map(member -> modelMapper.map(member, MemberInfoDto.class))
                .collect(Collectors.toList());
        return memberDtoList;
    }

    public List<MemberInfoDto> findAllMinorManager(String boardAddress) {
        List<Member> memberList = memberRepository.findMinorManager(boardAddress);
        return memberList.stream()
                .map(member -> modelMapper.map(member, MemberInfoDto.class))
                .collect(Collectors.toList());
    }
}
