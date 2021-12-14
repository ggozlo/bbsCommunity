package ggozlo.bbsCommunity.domain.member.authority;

public interface AuthorityJpaRepository {

    public Long persistAuthority(Authority authority);

    void deleteManager(Long username, String boardAddress);
}
