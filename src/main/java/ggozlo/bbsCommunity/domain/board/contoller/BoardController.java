package ggozlo.bbsCommunity.domain.board.contoller;

import ggozlo.bbsCommunity.domain.board.service.BoardService;
import ggozlo.bbsCommunity.global.dto.board.BoardDto;
import ggozlo.bbsCommunity.global.dto.board.BoardMainDto;
import ggozlo.bbsCommunity.global.dto.post.PostListDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;


    @GetMapping("/{boardAddress}")
    public String boardMain(@PathVariable String boardAddress, Model model,@PageableDefault Pageable pageable) {
        BoardMainDto boardMain = boardService.boardMain(boardAddress, pageable);
        model.addAttribute("board", boardMain);


        return "board/boardMain";
    }


    @GetMapping("/list")
    public String boardList(Model model) {

        List<BoardDto> boardDtoList = boardService.boardList();
        model.addAttribute("boardList", boardDtoList);
        return "/board/boardList";
    }


}
