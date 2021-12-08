package ggozlo.bbsCommunity.domain.board.service;

import ggozlo.bbsCommunity.domain.board.Board;
import ggozlo.bbsCommunity.domain.board.repository.BoardRepository;
import ggozlo.bbsCommunity.domain.member.Member;
import ggozlo.bbsCommunity.domain.member.authority.Authority;
import ggozlo.bbsCommunity.domain.member.authority.AuthorityRepository;
import ggozlo.bbsCommunity.domain.member.repository.MemberRepository;
import ggozlo.bbsCommunity.global.dto.board.BoardCreateDto;
import ggozlo.bbsCommunity.global.dto.board.BoardDto;
import ggozlo.bbsCommunity.global.dto.board.BoardMainDto;
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

    public BoardMainDto boardMain(String boardName, Pageable pageable) {
        BoardMainDto board = boardRepository
                .findBoardMain(boardName, pageable).orElseThrow(() -> new NotExistBoardException("Ex.Board.NotExist"));
        return board;
    }

    public List<BoardDto> boardList() {
        List<Board> activeBoardList = boardRepository.findActiveBoardList();
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
}
