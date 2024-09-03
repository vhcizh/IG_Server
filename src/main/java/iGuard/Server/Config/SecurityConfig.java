package iGuard.Server.Config;

import iGuard.Server.Service.admin.CustomAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.thymeleaf.extras.springsecurity6.dialect.SpringSecurityDialect;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        .anyRequest().permitAll()  // 모든 요청을 허용
                )
                .csrf(AbstractHttpConfigurer::disable  // CSRF 보호 설정
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // 상태 비저장 세션 관리
                )
                .formLogin(form -> form
                        .loginPage("/admin/login")  // 로그인 페이지 URL 설정 (하지만 로그인을 사용하지 않음)
                        .defaultSuccessUrl("/home", true)  // 로그인 성공 시 리다이렉트
                        .permitAll()  // 로그인 페이지 접근 허용
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")  // 로그아웃 URL
                        .logoutSuccessUrl("/home")  // 로그아웃 후 리다이렉트
                        .permitAll()  // 로그아웃 페이지 접근 허용
                );

        return http.build();
    }
}