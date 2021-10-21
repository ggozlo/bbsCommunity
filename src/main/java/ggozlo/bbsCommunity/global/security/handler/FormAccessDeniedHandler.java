package ggozlo.bbsCommunity.global.security.handler;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class FormAccessDeniedHandler implements AccessDeniedHandler {

    private String errorPage;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {

        String encodedMessage = URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
        String deniedUrl = errorPage + "?message=" + encodedMessage;
        // 거부된 경로에 예외 메시지를 파라미터로 추가함

        response.sendRedirect(deniedUrl);
        // 추가된 경로로 리다이렉트
    }

    public FormAccessDeniedHandler(String errorPage) {
        this.errorPage = errorPage;
    }
}
