package iGuard.Server.Config;

//import iGuard.Server.Service.admin.CustomAuthenticationFilter;
import iGuard.Server.Component.CustomAccessDeniedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public SecurityConfig(
            @Qualifier("memberUserDetailsService") UserDetailsService memberUserDetailsService,
            @Qualifier("companyUserDetailsService") UserDetailsService companyUserDetailsService) {
        this.memberUserDetailsService = memberUserDetailsService;
        this.companyUserDetailsService = companyUserDetailsService;
    }

    @Bean
    public SecurityFilterChain memberSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/common/**")
                .authorizeHttpRequests(req -> req
                        .requestMatchers("/common/css/**", "/common/js/**").permitAll() // resources/static 내의 자원 허용
                        .requestMatchers("/common/login", "/common/join", "/common/home", "/common/places", "common/password").permitAll()
                        .requestMatchers("/common/mypage/**", "common/email/**").hasRole("MEMBER")
                        .anyRequest().hasAnyRole("MEMBER", "ADMIN")
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
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .exceptionHandling((ex)->{
                    ex.accessDeniedHandler(new CustomAccessDeniedHandler()); // 403 에러 커스텀 핸들러
                })
                .csrf(Customizer.withDefaults())
                .userDetailsService(memberUserDetailsService);

        return http.build();
    }
    @Bean
    public SecurityFilterChain adminSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/admin/**")
                .authorizeHttpRequests(req -> req
                        .requestMatchers("/common/css/**").permitAll() // resources/static 내의 모든 자원 허용
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
                        .logoutSuccessUrl("/admin/login?logout") // 로그아웃 성공 후 이동할 페이지
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .headers(header -> header
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                )
                .userDetailsService(companyUserDetailsService);

        return http.build();
    }


}