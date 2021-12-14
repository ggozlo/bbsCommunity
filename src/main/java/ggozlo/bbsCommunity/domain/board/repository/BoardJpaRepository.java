package ggozlo.bbsCommunity.domain.board.repository;

import ggozlo.bbsCommunity.domain.board.Board;
import ggozlo.bbsCommunity.global.dto.board.BoardMainDto;
import ggozlo.bbsCommunity.global.dto.board.BoardModifyDto;
import ggozlo.bbsCommunity.global.dto.board.SearchType;
import org.springframework.data.domain.Pageable;


import java.util.List;
import java.util.Optional;

public interface BoardJpaRepository {

    String persistBoard(Board board);

    List<Board> findActiveBoardList();

    String  findBoardNameByAddress(String boardAddress);

    void boardActivation(String boardAddress);

    void boardDeactivation(String boardAddress);

    BoardModifyDto findModifyBoard(String boardAddress);

    void updateBoard(String boardAddress, BoardModifyDto modifyDto);

    List<Board> findBoardSearch(String parameter);

    Optional<BoardMainDto> findBoardMain(String boardAddress, Pageable pageable, String  searchType, String parameter);
}
