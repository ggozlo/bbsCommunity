package ggozlo.bbsCommunity.global.security.handler;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FormLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        String errorMessage = "Invalid ID or Password";

        if(exception instanceof BadCredentialsException) {
            errorMessage = "Invalid Username or Password";
        } else if(exception instanceof DisabledException) {
            errorMessage = "Locked";
        } else if (exception instanceof CredentialsExpiredException) {
            errorMessage = "Expired password";
        }// 예외별 에러메세지를 지정
        setDefaultFailureUrl("/login?error=true&exception="+ errorMessage);
        // 실패 url 을 설정
        super.onAuthenticationFailure(request, response, exception);
        // 부모객체의 실패 핸들러를 실행시킴
    }
}
