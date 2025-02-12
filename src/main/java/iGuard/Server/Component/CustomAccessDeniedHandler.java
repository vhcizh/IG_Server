package iGuard.Server.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.csrf.CsrfException;

import java.io.IOException;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // 어드민이 /common/mypage에 접근 시 /admin/mypage로 리다이렉트
        if (request.isUserInRole("ADMIN") && request.getRequestURI().startsWith("/common/mypage")) {
            response.sendRedirect("/admin/mypage");
        }
        // 기본 403 에러 발생
        response.sendError(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase());
    }
}
