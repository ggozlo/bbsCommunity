package ggozlo.bbsCommunity.domain.post.contoller;

import ggozlo.bbsCommunity.domain.member.Member;
import ggozlo.bbsCommunity.domain.post.service.PostService;
import ggozlo.bbsCommunity.global.dto.post.PostModifyFormDto;
import ggozlo.bbsCommunity.global.dto.post.PostViewDto;
import ggozlo.bbsCommunity.global.dto.post.PostWriteDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class PostController {

    private final PostService postService;

    @GetMapping("/{boardId}/write")
    @PreAuthorize("isAuthenticated()")
    public String writeForm(@PathVariable String boardId, @ModelAttribute("PostForm") PostWriteDto postDto) {
        return "post/writeForm";
    }

    @PostMapping("/{boardName}/write")
    @PreAuthorize("isAuthenticated()")
    public String write(@PathVariable String boardName, RedirectAttributes redirect,
                        @AuthenticationPrincipal(expression = "id") Long authorId, PostWriteDto postDto) {
        postDto.setBoardName(boardName);
        postDto.setAuthorId(authorId);
        Long postId = postService.write(postDto);
        redirect.addAttribute("postId", postId);
        return "redirect:/board/{boardName}/{postId}";
    }

    @GetMapping("/{boardName}/{postId}")
    public String view(@PathVariable String boardName, @PathVariable Long postId, Model model,
                       @AuthenticationPrincipal Member member) {
        PostViewDto viewDto = postService.view(postId);
        if (member != null) {
            if (member.getNickname().equals(viewDto.getAuthorNickname())) viewDto.setAuthor(true);
        }
        model.addAttribute("viewPost", viewDto);
        return "post/view";
    }

    @GetMapping("/{boardName}/{postId}/delete")
    @PreAuthorize("isAuthenticated() and #member.nickname.equals(#authorNickname)")
    public String deleteCheck(@PathVariable String boardName, @PathVariable Long postId, String authorNickname,
                              @AuthenticationPrincipal Member member, Model model) {
        model.addAttribute("postId", postId);
        model.addAttribute("boardName", boardName);
        String author = postService.findAuthor(postId);
        model.addAttribute("author", author);
        return "post/deleteCheck";
    }

    @PostMapping("/{boardName}/{postId}/delete")
    @PreAuthorize("isAuthenticated() and #member.username.equals(#author)")
    public String deletePost(@PathVariable String boardName, @PathVariable Long postId, String author,
                             @AuthenticationPrincipal Member member) {
        postService.deletePost(postId);
        return "redirect:/board/{boardName}";
    }

    // 수정 필요
    @GetMapping("/{boardName}/{postId}/modify")
    @PreAuthorize("isAuthenticated() and #member.nickname.equals(#authorNickname)")
    public String modifyForm(@PathVariable String boardName, @PathVariable Long postId, Model model) {

        PostModifyFormDto modifyTarget = postService.modifyTarget(postId);
        model.addAttribute("post", model);
        return "post/modifyForm";
    }

    // 수정 필요
    @PostMapping("/{boardName}/{postId}/modify")
    @PreAuthorize("isAuthenticated() and #member.nickname.equals(#authorNickname)")
    public String modifyPost(@PathVariable String boardName, @PathVariable Long postId, Model model) {

        PostModifyFormDto modifyTarget = postService.modifyTarget(postId);
        model.addAttribute("post", model);
        return "post/modifyForm";
    }
}
