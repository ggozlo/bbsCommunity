package ggozlo.bbsCommunity.global.security.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FormLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private RequestCache requestCache = new HttpSessionRequestCache();
    // 인터셉트 되기 전의 요청정보가 있음
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    // 리다이렉트 클래스

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        super.setDefaultTargetUrl("/");
        // 기존 요청정보가 없을경우 이동하는 url

        SavedRequest savedRequest = requestCache.getRequest(request, response);
        // 인증요청을 받기 전에 저장된 요청정보

        String jumpUrl = request.getParameter("jump");

        if (savedRequest != null) { // 요청정보가 존재한다면 해당 요청정보로 이동
            String targetUrl = savedRequest.getRedirectUrl();
            redirectStrategy.sendRedirect(request, response, targetUrl);
        }
        else { // 요청정보가 없다면 로그인창 이전 url 또는 저장된 기본 url 로 이동함

            if (jumpUrl != null) {
                redirectStrategy.sendRedirect(request, response, jumpUrl);
            } else {
                redirectStrategy.sendRedirect(request, response, getDefaultTargetUrl());
            }

        }
    }
}
