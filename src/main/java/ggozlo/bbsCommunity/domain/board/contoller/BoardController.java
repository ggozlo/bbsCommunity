package ggozlo.bbsCommunity.domain.board.contoller;

import ggozlo.bbsCommunity.domain.board.repository.BoardRepository;
import ggozlo.bbsCommunity.domain.board.service.BoardService;
import ggozlo.bbsCommunity.domain.member.Member;
import ggozlo.bbsCommunity.domain.member.authority.Authority;
import ggozlo.bbsCommunity.domain.member.service.MemberService;
import ggozlo.bbsCommunity.global.dto.board.BoardCreateDto;
import ggozlo.bbsCommunity.global.dto.board.BoardDto;
import ggozlo.bbsCommunity.global.dto.board.BoardMainDto;
import ggozlo.bbsCommunity.global.dto.board.BoardModifyDto;
import ggozlo.bbsCommunity.global.dto.member.MemberInfoDto;
import ggozlo.bbsCommunity.global.security.detailsService.SessionMember;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;
    private final BoardRepository boardRepository;
    private final MemberService memberService;

    @GetMapping("/{boardAddress}")
    public String boardMain(@PathVariable String boardAddress, Model model, @PageableDefault Pageable pageable,
                            @AuthenticationPrincipal Member member, String parameter, String type,
                            HttpServletResponse response, HttpServletRequest request) {


        BoardMainDto boardMain = boardService.boardMain(boardAddress, pageable, type, parameter);
        model.addAttribute("board", boardMain);

        Cookie cookie = new Cookie("b_"+boardAddress, boardMain.getBoardName());
        cookie.setMaxAge(60*60*24);
        response.addCookie(cookie);

        Cookie[] cookies = request.getCookies();
        List<Cookie> cookieList = Arrays.stream(cookies).filter(c -> c.getName().startsWith("b_")).collect(Collectors.toList());
        model.addAttribute("cookieList", cookieList);


        if (member != null) {
            model.addAttribute("isPrime", checkPrime(boardAddress, member));
        }

        return "board/boardMain";
    }


    @GetMapping("/list")
    public String boardList(Model model) {

        List<BoardDto> boardDtoList = boardService.boardList();
        model.addAttribute("boardList", boardDtoList);
        return "/board/boardList";
    }

    @GetMapping("/search")
    public String boardSearch(Model model, String parameter) {

        List<BoardDto> boardDtoList = boardService.boardSearch(parameter);
        model.addAttribute("boardList", boardDtoList);
        return "/board/boardList";
    }


    // 게시판 삭제 페이지이동
    @GetMapping("/{boardAddress}/delete")
    @PreAuthorize("hasRole('admin')")
    public String boardDeleteForm(@PathVariable String boardAddress, Model model) {
        return "/board/deleteCheck";
    }

    // 게시판 삭제처리
    @PostMapping("/{boardAddress}/delete")
    @PreAuthorize("hasRole('admin')")
    public String boardDelete(@PathVariable String boardAddress) {
        boardService.deleteBoard(boardAddress);
        return "redirect:/board/list";
    }



    // 게시판 생성
    @GetMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public String boardCreateForm(@ModelAttribute("boardForm") BoardCreateDto boardCreateDto) {
        return "/board/createForm";
    }

    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public String boardCreateProc(BoardCreateDto boardDto, RedirectAttributes redirect,
                                  @AuthenticationPrincipal Member member) {
        boardDto.setApplicantId(member.getId());
        String boardAddress = boardService.createBoard(boardDto);
        redirect.addAttribute("boardAddress",boardAddress);


        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();
        ArrayList<GrantedAuthority> updateAuthorities = new ArrayList<>(authentication
                .getAuthorities());
        updateAuthorities.add(new SimpleGrantedAuthority("ROLE_" + boardAddress + "_Prime"));

        member.getAuthorityList().add(new Authority(null, null, boardAddress + "_Prime"));

        UsernamePasswordAuthenticationToken updateToken = new UsernamePasswordAuthenticationToken(member, null, updateAuthorities);
        SecurityContextHolder.getContext().setAuthentication(updateToken);

        return "redirect:/board/{boardAddress}";
    }


    @GetMapping("/{boardAddress}/modify")
    @PreAuthorize("isAuthenticated() and hasAnyRole('admin', #boardAddress + '_Prime')")
    public String boardModifyForm(@PathVariable String boardAddress, Model model) {
        BoardModifyDto modifyForm = boardService.modifyBoardTarget(boardAddress);
        model.addAttribute("modifyForm", modifyForm);
        return "/board/modifyForm";
    }

    @PostMapping("/{boardAddress}/modify")
    @PreAuthorize("isAuthenticated() and hasAnyRole('admin', #boardAddress + '_Prime')")
    public String boardModify(BoardModifyDto modifyDto, @PathVariable String boardAddress) {
        boardService.modifyBoard(modifyDto, boardAddress);
        return "redirect:/board/{boardAddress}";
    }

    @GetMapping("/{boardAddress}/lock")
    @PreAuthorize("hasRole('admin')")
    public String boardLock(@PathVariable String boardAddress, Model model) {
        model.addAttribute("isActive", boardRepository.isActiveBoard(boardAddress));
        return "/board/lockCheck";
    }

    @PostMapping("/{boardAddress}/lock")
    @PreAuthorize("hasRole('admin')")
    public String boardLockProc(@PathVariable String boardAddress, boolean isActive) {
        if (isActive) {
            boardService.deactivateBoard(boardAddress);
        } else {
            boardService.activateBoard(boardAddress);
        }
        return "redirect:/board/{boardAddress}";
    }

    @GetMapping("/{boardAddress}/addManager")
    @PreAuthorize("isAuthenticated() and hasAnyRole('admin', #boardRepository+'_Prime')")
    public String addMinorManager(@PathVariable String boardAddress, @AuthenticationPrincipal Member member) {
        return "/board/addManagerForm";
    }

    @PostMapping("/{boardAddress}/addManager")
    @PreAuthorize("isAuthenticated() and hasAnyRole('admin', #boardRepository+'_Prime')")
    public String addManagerProc(@PathVariable String boardAddress, String nickname) {
        boardService.addManager(nickname, boardAddress);
        return "redirect:/board/{boardAddress}";
    }

    @GetMapping("/{boardAddress}/deleteManager")
    @PreAuthorize("isAuthenticated() and hasAnyRole('admin', #boardRepository+'_Prime')")
    public String deleteMinorManager(@PathVariable String boardAddress, Model model) {
        List<MemberInfoDto> memberList = memberService.findAllMinorManager(boardAddress);
        model.addAttribute("memberList", memberList);
        return "/board/deleteManagerForm";
    }

    @PostMapping("/{boardAddress}/deleteManager")
    @PreAuthorize("isAuthenticated() and hasAnyRole('admin', #boardRepository+'_Prime')")
    public String deleteMinorManagerProc(@PathVariable String boardAddress, Model model, Long userId) {
        boardService.deleteManager(userId, boardAddress);

        return "redirect:/board/{boardAddress}";
    }


    private boolean checkPrime(String boardAddress, Member member)    {

            String primeBoardAuth = boardAddress + "_Prime";
            Optional<Authority> auth = member.getAuthorityList().stream().filter(authority -> {
                String role = authority.getRole();
                if (role.equals(primeBoardAuth) || role.equals("admin")) {
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
