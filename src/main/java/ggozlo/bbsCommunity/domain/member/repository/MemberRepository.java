package ggozlo.bbsCommunity.domain.member.repository;

import ggozlo.bbsCommunity.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface MemberRepository extends JpaRepository<Member, Long>, MemberJpaRepository {


    Optional<Member> findByUsername(String username);

    int countByUsername(String username);

    List<Member> findByUsernameContaining(String username);

    Optional<Member> findByNickname(String authorNickname);
}
