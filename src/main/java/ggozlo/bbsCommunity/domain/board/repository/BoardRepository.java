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
    @Query("select b.name from Board b where b.address = :address")
    String findBoardNameByAddress(@Param("address") String address);
}
