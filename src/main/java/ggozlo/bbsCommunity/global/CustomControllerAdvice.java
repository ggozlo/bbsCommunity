package ggozlo.bbsCommunity.global;

import ggozlo.bbsCommunity.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class CustomControllerAdvice {

    private final MessageSource messageSource;


    @ExceptionHandler(CustomException.class) // 직접 생성한 예외 처리용
    public String exceptionHandling(Model model, CustomException e, HttpServletRequest request) {
        log.debug("customExceptionHandler Called!");
        log.warn("Exception Message : {} ", e.getMessage());
        String message = messageSource
                .getMessage(e.getMessage(), null,"오류가 발생했습니다.", request.getLocale());
        model.addAttribute("message", message);
        return "common/error";
    }

}
