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
        // session 만료시 로그아웃 하면 로그인 페이지로 이동
        else if (request.getRequestURI().equals("/common/logout") || request.getRequestURI().equals("/admin/logout")) {
            response.sendRedirect("/common/login");
        }
        // 기본 403 에러 발생
        else {
            response.sendError(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase());
        }
    }
}
