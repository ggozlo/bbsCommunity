package ggozlo.bbsCommunity.domain.comment.contoller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ggozlo.bbsCommunity.domain.comment.service.CommentService;
import ggozlo.bbsCommunity.domain.member.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;
    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;

    @PostMapping("/{boardAddress}/{postId}/write")
    @PreAuthorize("isAuthenticated()")
    public String commentWrite(@PathVariable Long postId, @PathVariable String boardAddress, String comment,
                               @AuthenticationPrincipal Member member) {
        commentService.saveComment(member.getId(), postId, comment);
        return "redirect:/board/{boardAddress}/{postId}";
    }

    @GetMapping("/{boardAddress}/{postId}/delete")
    @PreAuthorize("isAuthenticated()")
    public String commentDelete(@PathVariable String boardAddress, @PathVariable Long postId, Long commentId) {

        commentService.deleteComment(commentId);
        return "redirect:/board/{boardAddress}/{postId}";
    }

}
