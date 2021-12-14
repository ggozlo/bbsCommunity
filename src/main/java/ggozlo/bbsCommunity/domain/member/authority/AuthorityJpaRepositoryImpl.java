package ggozlo.bbsCommunity.domain.member.authority;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@RequiredArgsConstructor
public class AuthorityJpaRepositoryImpl implements  AuthorityJpaRepository{

    @PersistenceContext
    EntityManager manager;

    private QAuthority qAuthority = QAuthority.authority;

    private final JPAQueryFactory queryFactory;

    @Transactional
    public Long persistAuthority(Authority authority) {
        manager.persist(authority);
        return authority.getId();
    }

    @Override
    public void deleteManager(Long userId, String boardAddress) {
        queryFactory
                .delete(qAuthority)
                .where(qAuthority.member.id.eq(userId),
                        qAuthority.board.address.eq(boardAddress))
                .execute();
    }
}
