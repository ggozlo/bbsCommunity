package ggozlo.bbsCommunity.domain.board.repository;

import ggozlo.bbsCommunity.domain.board.Board;
import ggozlo.bbsCommunity.global.dto.board.BoardMainDto;

import java.util.Optional;

public interface BoardJpaRepository {

    public String persistBoard(Board board);

    Optional<BoardMainDto> findBoardMain(String boardName);
}
