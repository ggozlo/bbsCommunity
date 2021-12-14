package ggozlo.bbsCommunity.domain.board.repository;

import ggozlo.bbsCommunity.domain.board.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface BoardRepository extends JpaRepository<Board, String>, BoardJpaRepository {

    @Transactional(readOnly = true)
    @Query("select case when b.activation = true then true else  false  end from Board b where b.address = :boardAddress")
    boolean isActiveBoard(@Param("boardAddress") String boardAddress);

}
