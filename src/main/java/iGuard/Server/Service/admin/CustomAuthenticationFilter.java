package iGuard.Server.Service.admin;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {


    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        String uri = request.getRequestURI();
        if (uri.startsWith("/admin")) {
            setUsernameParameter("email");
            setPasswordParameter("admin_password");
        } else {
            setUsernameParameter("user_id");
            setPasswordParameter("user_password");
        }
        return super.attemptAuthentication(request, response);
    }
}