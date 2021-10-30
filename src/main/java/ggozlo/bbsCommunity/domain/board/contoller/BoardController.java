package ggozlo.bbsCommunity.domain.board.contoller;

import ggozlo.bbsCommunity.domain.board.service.BoardService;
import ggozlo.bbsCommunity.global.dto.board.BoardMainDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;


    @GetMapping("/{boardName}")
    public String boardMain(@PathVariable String boardName, Model model) {
        BoardMainDto boardMain = boardService.boardMain(boardName);
        model.addAttribute("board", boardMain);
        return "board/boardMain";
    }


}
