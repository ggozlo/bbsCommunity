package ggozlo.bbsCommunity.global;

import ggozlo.bbsCommunity.domain.board.service.BoardService;
import ggozlo.bbsCommunity.domain.member.Member;
import ggozlo.bbsCommunity.domain.member.service.MemberService;
import ggozlo.bbsCommunity.domain.post.service.PostService;
import ggozlo.bbsCommunity.global.dto.board.BoardCreateDto;
import ggozlo.bbsCommunity.global.dto.member.MemberJoinDto;
import ggozlo.bbsCommunity.global.dto.post.PostWriteDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class Initializer {

    private final MemberService memberService;
    private final BoardService boardService;
    private final PostService postService;

    @EventListener(ApplicationReadyEvent.class)
    public void init(){
        MemberJoinDto adminDto = new MemberJoinDto("admin", "1234", "ggozlo@naver.com", "adminUser");
        Long adminId = memberService.join(adminDto);
        BoardCreateDto noticeBoardDto = new BoardCreateDto("notice", "공지사항", "BBSCommunity 공지게시판 입니다.", adminId);
        String noticeBoard = boardService.createBoard(noticeBoardDto);
        PostWriteDto noticePostDto = new PostWriteDto("공지 제목입니다.", "공지사항 본문입니다.", adminId, noticeBoard,0);
        postService.write(noticePostDto);
        log.debug("Success Initialize!");
    }
}
