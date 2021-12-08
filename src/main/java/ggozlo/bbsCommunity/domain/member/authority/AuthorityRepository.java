package ggozlo.bbsCommunity.domain.member.authority;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> , AuthorityJpaRepository {

}
