package ggozlo.bbsCommunity.global;


import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;

@Slf4j
@Controller
public class HomeController {

    @RequestMapping("/")
    public String index(Model model, HttpServletRequest request) {
        model.addAttribute("message", "Welcome!");
        return "home";
    }

    @RequestMapping(value = "/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "exception", required = false) String exception,
                        @RequestParam(value = "jump", required = false) String jump,
                        Model model, Principal principal) {
        if (principal != null) {
            return "/home";
        }
        model.addAttribute("jump", jump);
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);
        return "/common/login";
    }

    @RequestMapping("/denied") //
    public String accessDeny(@RequestParam(required = false) String message, Model model) {
        model.addAttribute("message", message);
        return "/common/denied";
    }

    @GetMapping("/logout") // get 방식의 로그아웃 직접 구현
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        // 로그아웃을 위한 서블릿을 인자로 받음
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // SecurityContextHolder 에서 인증객체를 받아옴
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
            // 인증객체가 존재한다면 로그아웃 핸들러를 생성해 로그아웃처리
        }
        return "redirect:/login"; // 작동안함
    }

    @GetMapping("/test")
    @PreAuthorize("isAuthenticated()")
    public String test(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.info("requestURI = {}", requestURI);

        return "/home";

    }
}
