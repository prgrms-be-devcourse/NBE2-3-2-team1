package org.programmers.cocktail.global.aop;


import jakarta.servlet.http.HttpSession;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.programmers.cocktail.global.exception.UnauthorizedException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

@Aspect
@Component
public class LoginCheckAspect {

    @Before("execution(* org.programmers.cocktail.search.controller.*.*(..))&& @annotation(org.programmers.cocktail.global.annotation.RequireLogin)")
    public void loginCheck() {
        // 현재 요청의 Session 가져오기
        HttpSession session = (HttpSession) RequestContextHolder.currentRequestAttributes()
            .resolveReference(RequestAttributes.REFERENCE_SESSION);

        if(session == null || session.getAttribute("semail") == null) {
            //세션이 없거나 로그인이 안된상태로 확인된 경우 예외 발생
            //globalExceptionHandler에서 HTTP 401 반환하도록 처리 필요
            throw new UnauthorizedException();
        }
    }
}