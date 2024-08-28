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

    private final UserDetailsService memberUserDetailsService;
    private final UserDetailsService companyUserDetailsService;


    @Bean
    public SpringSecurityDialect springSecurityDialect() {
        return new SpringSecurityDialect();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Autowired
    public SecurityConfig(
            @Qualifier("memberUserDetailsService") UserDetailsService memberUserDetailsService,
            @Qualifier("companyUserDetailsService") UserDetailsService adminUserDetailsService) {
        this.memberUserDetailsService = memberUserDetailsService;
        this.companyUserDetailsService = adminUserDetailsService;
    }

    @Bean
    public SecurityFilterChain memberSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/common/**")
                .authorizeHttpRequests(req -> req
                        .requestMatchers("/static/**").permitAll() // resources/static 내의 모든 자원 허용
                        .requestMatchers("/login", "/join", "/home").permitAll()
                        .anyRequest().hasRole("MEMBER")
                )
                .formLogin(form -> form
                        .loginPage("/common/login")
                        .defaultSuccessUrl("/common/home", true)
                        .usernameParameter("id")
                        .passwordParameter("password")
                        .failureUrl("/common/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/common/logout") // 회원 로그아웃 URL
                        .logoutSuccessUrl("/common/login?logout") // 로그아웃 성공 후 이동할 페이지
                        .permitAll()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .userDetailsService(memberUserDetailsService);

        return http.build();
    }
    @Bean
    public SecurityFilterChain adminSecurityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        http
                .securityMatcher("/admin/**")
                .authorizeHttpRequests(req -> req
                        .requestMatchers("/static/**").permitAll() // resources/static 내의 모든 자원 허용
                        .requestMatchers("/admin/login", "/admin/register", "/admin/verify").permitAll()
                        .anyRequest().hasRole("ADMIN")
                )
                .formLogin(form -> form
                        .loginPage("/admin/login")
                        .defaultSuccessUrl("/admin/mypage", true)
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .failureUrl("/admin/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/admin/logout") // 회원 로그아웃 URL
                        .logoutSuccessUrl("/admin/login?logout=true") // 로그아웃 성공 후 이동할 페이지
                        .permitAll()
                )
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .headers(header -> header
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .invalidSessionUrl("/login")
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(false)
                )
                .rememberMe(rememberMe -> rememberMe
                        .userDetailsService(this.companyUserDetailsService)
                        .key("uniqueAndSecret")
                        .tokenValiditySeconds(86400) // 24 hours
                )
                .addFilter(new CustomAuthenticationFilter(authenticationManager))
                .userDetailsService(companyUserDetailsService);

        return http.build();
    }


}