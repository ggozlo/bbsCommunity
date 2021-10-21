package ggozlo.bbsCommunity.domain.member.repository;

import ggozlo.bbsCommunity.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>, MemberJpaRepository {

    Optional<Member> findByUsername(String username);

    boolean existsByUsername(String loginId);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);

    boolean existsByUsernameOrEmailOrNickname(String loginId, String email, String nickname);

    int countByUsername(String username);
    List<Member> findByUsernameContaining(String username);
}
