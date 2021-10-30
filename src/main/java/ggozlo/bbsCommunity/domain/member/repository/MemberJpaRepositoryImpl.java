package ggozlo.bbsCommunity.domain.member.repository;

import com.querydsl.jpa.impl.JPAInsertClause;
import com.querydsl.jpa.impl.JPAQueryFactory;
import ggozlo.bbsCommunity.domain.member.Member;
import ggozlo.bbsCommunity.domain.member.QMember;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;

@Transactional
@RequiredArgsConstructor
public class MemberJpaRepositoryImpl implements MemberJpaRepository{

    @PersistenceContext
    EntityManager manager;

    private final JPAQueryFactory queryFactory;
    private final QMember qMember = QMember.member;

    @Override
    public Member persistMember(Member member) {
        manager.persist(member);
        return member;
    }

    @Override
    public void updateUsername(Long memberId, String newUsername) {
        queryFactory
                .update(qMember)
                .set(qMember.username, newUsername)
                .set(qMember.lastModifiedDate, LocalDateTime.now())
                .where(qMember.id.eq(memberId))
                .execute();
    }

    @Override
    public void updatePassword(Long memberId, String newPassword) {
        queryFactory
                .update(qMember)
                .set(qMember.password, newPassword)
                .set(qMember.lastModifiedDate, LocalDateTime.now())
                .where(qMember.id.eq(memberId))
                .execute();
    }

    @Override
    public void updateEmail(Long memberId, String newEmail) {
        queryFactory
                .update(qMember)
                .set(qMember.email, newEmail)
                .set(qMember.lastModifiedDate, LocalDateTime.now())
                .where(qMember.id.eq(memberId))
                .execute();
    }
    @Override
    public void updateNickname(Long memberId, String newNickname) {
        queryFactory
                .update(qMember)
                .set(qMember.nickname, newNickname)
                .set(qMember.lastModifiedDate, LocalDateTime.now())
                .where(qMember.id.eq(memberId))
                .execute();
    }


}
