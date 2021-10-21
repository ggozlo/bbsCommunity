package ggozlo.bbsCommunity.domain.member.repository;

import ggozlo.bbsCommunity.domain.member.Member;

public interface MemberJpaRepository {

    Member persistMember(Member member);

    void updateUsername(Long memberId, String newUsername);
    void updatePassword(Long memberId, String newPassword);
    void updateEmail(Long memberId, String newEmail);
    void updateNickname(Long memberId, String newNickname);
}
