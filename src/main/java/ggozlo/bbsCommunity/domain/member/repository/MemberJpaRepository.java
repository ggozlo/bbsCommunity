package ggozlo.bbsCommunity.domain.member.repository;

import ggozlo.bbsCommunity.domain.member.Member;

import java.util.List;

public interface MemberJpaRepository {

    Member persistMember(Member member);

    void updateUsername(Long memberId, String newUsername);
    void updatePassword(Long memberId, String newPassword);
    void updateEmail(Long memberId, String newEmail);
    void updateNickname(Long memberId, String newNickname);

    List<Member> findMinorManager(String boardAddress);
}
