package ggozlo.bbsCommunity.domain.member.repository;

import com.querydsl.jpa.impl.JPAInsertClause;
import com.querydsl.jpa.impl.JPAQueryFactory;
import ggozlo.bbsCommunity.domain.member.Member;
import ggozlo.bbsCommunity.domain.member.QMember;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@RequiredArgsConstructor
public class MemberJpaRepositoryImpl implements MemberJpaRepository{

    @PersistenceContext
    EntityManager manager;

    private final JPAQueryFactory queryFactory;
    private final QMember qMember = QMember.member;

    @Override
    @Transactional
    public Member persistMember(Member member) {
        manager.persist(member);
        return member;
    }

    @Override
    @Transactional
    public void updateUsername(Long memberId, String newUsername) {
        queryFactory
                .update(qMember)
                .set(qMember.username, newUsername)
                .where(qMember.id.eq(memberId))
                .execute();
    }

    @Override
    public void updatePassword(Long memberId, String newPassword) {
        queryFactory
                .update(qMember)
                .set(qMember.password, newPassword)
                .where(qMember.id.eq(memberId))
                .execute();
    }

    @Override
    public void updateEmail(Long memberId, String newEmail) {
        queryFactory
                .update(qMember)
                .set(qMember.email, newEmail)
                .where(qMember.id.eq(memberId))
                .execute();
    }
    @Override
    public void updateNickname(Long memberId, String newNickname) {
        queryFactory
                .update(qMember)
                .set(qMember.nickname, newNickname)
                .where(qMember.id.eq(memberId))
                .execute();
    }


}
