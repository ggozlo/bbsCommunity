package ggozlo.bbsCommunity.domain.board.repository;

import ggozlo.bbsCommunity.domain.board.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, String>, BoardJpaRepository {
}
