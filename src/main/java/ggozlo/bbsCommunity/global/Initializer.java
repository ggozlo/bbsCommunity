package ggozlo.bbsCommunity.global;

import ggozlo.bbsCommunity.domain.board.service.BoardService;
import ggozlo.bbsCommunity.domain.member.Member;
import ggozlo.bbsCommunity.domain.member.authority.Authority;
import ggozlo.bbsCommunity.domain.member.authority.AuthorityRepository;
import ggozlo.bbsCommunity.domain.member.service.MemberService;
import ggozlo.bbsCommunity.domain.post.service.PostService;
import ggozlo.bbsCommunity.global.dto.board.BoardCreateDto;
import ggozlo.bbsCommunity.global.dto.member.MemberJoinDto;
import ggozlo.bbsCommunity.global.dto.post.PostWriteDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${admin.username}")
    private String adminUsername;

    @EventListener(ApplicationReadyEvent.class)
    public void init(){
        Long adminId = memberService.createAdmin(adminUsername, "1234", "ggozlo@naver.com", "운영자");

        BoardCreateDto noticeBoardDto = new BoardCreateDto("notice", "공지사항", "BBSCommunity 공지게시판 입니다.", adminId);
        String noticeBoard = boardService.createBoard(noticeBoardDto);
        PostWriteDto noticePostDto = new PostWriteDto("공지 제목입니다.", "공지사항 본문입니다.", adminId, noticeBoard,0);
        postService.write(noticePostDto);

        log.debug("Success Initialize!");
    }
}
