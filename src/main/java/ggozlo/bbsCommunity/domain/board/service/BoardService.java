package ggozlo.bbsCommunity.domain.board.service;

import ggozlo.bbsCommunity.domain.board.Board;
import ggozlo.bbsCommunity.domain.board.repository.BoardRepository;
import ggozlo.bbsCommunity.domain.member.Member;
import ggozlo.bbsCommunity.domain.member.authority.Authority;
import ggozlo.bbsCommunity.domain.member.authority.AuthorityRepository;
import ggozlo.bbsCommunity.domain.member.repository.MemberRepository;
import ggozlo.bbsCommunity.global.dto.board.*;
import ggozlo.bbsCommunity.global.exception.board.BoardDisabledException;
import ggozlo.bbsCommunity.global.exception.board.NotExistBoardException;
import ggozlo.bbsCommunity.global.exception.member.NotFoundMemberException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final AuthorityRepository authorityRepository;
    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;

    public String createBoard(BoardCreateDto boardDto) {

        Member member = memberRepository.findById(boardDto.getApplicantId())
                .orElseThrow(() -> new NotFoundMemberException("Ex.Member.NotFoundMember"));

        Board board = modelMapper.map(boardDto, Board.class);
        board.setActivation(true);
        boardRepository.persistBoard(board);

        authorityRepository.persistAuthority(new Authority(member, board, board.getAddress() + "_Prime"));

        return board.getAddress();
    }


    public BoardMainDto boardMain(String boardAddress, Pageable pageable, String type, String parameter) {
        BoardMainDto board = boardRepository
                .findBoardMain(boardAddress, pageable, type, parameter).orElseThrow(() -> new NotExistBoardException("Ex.Board.NotExist"));
        return board;
    }

    public List<BoardDto> boardList() {
        List<Board> activeBoardList = boardRepository.findActiveBoardList();

        List<BoardDto> boardDto = toBoardDtoList(activeBoardList);

        return boardDto;
    }



    public boolean checkBoardActivated(String boardAddress) {
        boolean isActive = boardRepository.isActiveBoard(boardAddress);
        if (!isActive) {
            throw new BoardDisabledException("Ex.Board.Disabled");
        } else {
            return true;
        }
    }

    public void activateBoard(String boardAddress) {
        boardRepository.boardActivation(boardAddress);
    }

    public void deactivateBoard(String boardAddress) {
        boardRepository.boardDeactivation(boardAddress);
    }

    public void deleteBoard(String boardAddress) {
        boardRepository.deleteById(boardAddress);
    }

    public BoardModifyDto modifyBoardTarget(String boardAddress) {
        return boardRepository.findModifyBoard(boardAddress);
    }

    public void modifyBoard(BoardModifyDto modifyDto, String boardAddress) {
        boardRepository.updateBoard(boardAddress, modifyDto);
    }

    public List<BoardDto> boardSearch(String parameter) {
        List<Board> boardSearch = boardRepository.findBoardSearch(parameter);
        List<BoardDto> boardDtoList = toBoardDtoList(boardSearch);
        return boardDtoList;
    }

    private List<BoardDto> toBoardDtoList(List<Board> activeBoardList) {
        return activeBoardList
                .stream()
                .map(board -> new BoardDto(
                        board.getAddress(),
                        board.getName(),
                        board.getDescribe(),
                        board.getAuthorityList()
                                .stream()
                                .filter(authority -> authority
                                        .getRole()
                                        .equals(board.getAddress() + "_Prime"))
                                .findFirst().get().getMember().getNickname(),
                        board.getCreateDate()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public void addManager(String nickname, String boardAddress) {
        Member member = memberRepository.findByNickname(nickname).orElseThrow(() -> new NotFoundMemberException("Ex.Member.NotFound"));
        Board board = boardRepository.findById(boardAddress).orElseThrow(() -> new NotExistBoardException("Ex.Board.NotExist"));
        Authority authority = new Authority(member, board, boardAddress + "_Minor");
        authorityRepository.persistAuthority(authority);
    }

    public void deleteManager(Long username, String boardAddress) {
        authorityRepository.deleteManager(username, boardAddress);
    }
}
