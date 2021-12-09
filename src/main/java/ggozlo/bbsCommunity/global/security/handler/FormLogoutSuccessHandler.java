package ggozlo.bbsCommunity.global.security.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FormLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        String jumpUrl = request.getParameter("jump");

        if (jumpUrl == null) {
            redirectStrategy.sendRedirect(request, response, getDefaultTargetUrl());
        } else {
            redirectStrategy.sendRedirect(request, response, jumpUrl);
        }
    }
}
