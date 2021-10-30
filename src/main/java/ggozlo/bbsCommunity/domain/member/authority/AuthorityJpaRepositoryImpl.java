package ggozlo.bbsCommunity.domain.member.authority;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@RequiredArgsConstructor
public class AuthorityJpaRepositoryImpl implements  AuthorityJpaRepository{

    @PersistenceContext
    EntityManager manager;

    @Transactional
    public Long persistAuthority(Authority authority) {
        manager.persist(authority);
        return authority.getId();
    }
}
