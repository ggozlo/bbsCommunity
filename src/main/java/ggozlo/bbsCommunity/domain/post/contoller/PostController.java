package ggozlo.bbsCommunity.domain.post.contoller;

import ggozlo.bbsCommunity.domain.board.repository.BoardRepository;
import ggozlo.bbsCommunity.domain.board.service.BoardService;
import ggozlo.bbsCommunity.domain.comment.service.CommentService;
import ggozlo.bbsCommunity.domain.member.Member;
import ggozlo.bbsCommunity.domain.member.authority.Authority;
import ggozlo.bbsCommunity.domain.post.service.PostService;
import ggozlo.bbsCommunity.global.dto.comment.CommentDto;
import ggozlo.bbsCommunity.global.dto.post.PostModifyFormDto;
import ggozlo.bbsCommunity.global.dto.post.PostViewDto;
import ggozlo.bbsCommunity.global.dto.post.PostWriteDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;


@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class PostController {

    private final PostService postService;
    private final BoardService boardService;
    private final BoardRepository boardRepository;
    private final CommentService commentService;

    @Value("${admin.username}")
    private String adminUsername;

    @GetMapping("/{boardAddress}/write")
    @PreAuthorize("isAuthenticated()")
    public String writeForm(@PathVariable String boardAddress, @ModelAttribute("PostForm") PostWriteDto postDto, Model model) {
        boardService.checkBoardActivated(boardAddress);
        model.addAttribute("boardName", boardRepository.findBoardNameByAddress(boardAddress));
        return "post/writeForm";
    }

    @PostMapping("/{boardAddress}/write")
    @PreAuthorize("isAuthenticated()")
    public String writeProc(@PathVariable String boardAddress, RedirectAttributes redirect,
                        @AuthenticationPrincipal(expression = "id") Long authorId, PostWriteDto postDto) {
        boardService.checkBoardActivated(boardAddress);
        postDto.setBoardAddress(boardAddress);
        postDto.setAuthorId(authorId);
        postDto.setViews(0);
        Long postId = postService.write(postDto);
        redirect.addAttribute("postId", postId);
        return "redirect:/board/{boardAddress}/{postId}";
    }

    @GetMapping("/{boardAddress}/{postId}")
    public String view(@PathVariable String boardAddress, @PathVariable Long postId, Model model,
                       @AuthenticationPrincipal Member member) {
        boolean isActive = boardService.checkBoardActivated(boardAddress);
        PostViewDto viewDto = postService.view(postId);

        if (member != null) {
            viewDto.setAuthor(checkAuth(boardAddress, member, viewDto.getAuthorId()));
        }

        List<CommentDto> commentList = commentService.postComment(postId, (member==null)? "" : member.getNickname());

        model.addAttribute("commentList", commentList);
        model.addAttribute("isActive", isActive);
        model.addAttribute("viewPost", viewDto);

        return "post/view";
    }


    //

    @GetMapping("/{boardAddress}/{postId}/delete")
    @PreAuthorize("isAuthenticated() and  hasAnyRole(#boardAddress + '_Prime', #boardAddress + '_Minor', 'admin'," +
            "@postService.findAuthor(#postId) )" )
    public String deleteCheck(@PathVariable String boardAddress, @PathVariable Long postId, String authorNickname,
                              @AuthenticationPrincipal Member member, Model model) {
        return "post/deleteCheck";
    }
    @PostMapping("/{boardAddress}/{postId}/delete")
    @PreAuthorize("isAuthenticated() and  hasAnyRole(#boardAddress + '_Prime', #boardAddress + '_Minor', 'admin'," +
            "@postService.findAuthor(#postId) )" )
    public String deletePost(@PathVariable String boardAddress, @PathVariable Long postId, String author,
                             @AuthenticationPrincipal Member member) {
        boardService.checkBoardActivated(boardAddress);
        postService.deletePost(postId);
        return "redirect:/board/{boardAddress}";
    }

    ///


    @GetMapping("/{boardAddress}/{postId}/modify")
    @PreAuthorize("isAuthenticated() and  hasAnyRole(#boardAddress + '_Prime', #boardAddress + '_Minor', 'admin'," +
            "@postService.findAuthor(#postId) )" )
    public String modifyForm(@PathVariable String boardAddress, @PathVariable Long postId, Model model) {
        boardService.checkBoardActivated(boardAddress);
        PostModifyFormDto post = postService.findModifyTarget(postId);
        model.addAttribute("modifyTarget", post);
        return "post/modifyForm";
    }


    @PostMapping("/{boardAddress}/{postId}/modify")
    @PreAuthorize("isAuthenticated() and  hasAnyRole(#boardAddress + '_Prime', #boardAddress + '_Minor', 'admin'," +
            "@postService.findAuthor(#postId) )" )
    public String modifyPost(@PathVariable String boardAddress, @PathVariable Long postId, PostModifyFormDto postModifyDto) {
        boardService.checkBoardActivated(boardAddress);
        postService.modifyPost(postModifyDto, postId);
        return "redirect:/board/{boardAddress}/{postId}";
    }


    // 글 수정, 삭제 권한체크용 메서드
    private boolean checkAuth(String boardAddress, Member member, Long authorId) {
        if (member.getId().equals(authorId)) {
            return true;
        } else {
            String primeBoardAuth = boardAddress + "_Prime";
            String minorBoardAuth = boardAddress + "_Minor";

            Optional<Authority> auth = member.getAuthorityList().stream().filter(authority -> {
                String role = authority.getRole();
                if (role.equals(primeBoardAuth) || role.equals(minorBoardAuth) || role.equals(adminUsername)) {
                    return true;
                }
                else return false;
            }).findFirst();

            if (auth.isPresent()) {
                return true;
            }
            return false;
        }
    }



}
