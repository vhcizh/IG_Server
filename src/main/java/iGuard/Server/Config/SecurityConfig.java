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
                .authorizeHttpRequests(req -> req
                        .requestMatchers("/login", "/join", "/home").permitAll()
                        .anyRequest().hasRole("MEMBER")
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/home", true)
                        .usernameParameter("id")
                        .passwordParameter("password")
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                // 기타 필요한 설정...
                .userDetailsService(memberUserDetailsService);

        return http.build();
    }

    @Bean
    public SecurityFilterChain adminSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/admin/**")
                .authorizeHttpRequests(req -> req
                        .requestMatchers("/admin/login", "/admin/register", "/admin/verify").permitAll()
                        .anyRequest().hasRole("ADMIN")
                )
                .formLogin(form -> form
                        .loginPage("/admin/login")
                        .defaultSuccessUrl("/admin/home", true)
                        .failureUrl("/admin/login?error=true")
                        .permitAll()
                )
                // 기타 필요한 설정...
                .userDetailsService(companyUserDetailsService);

        return http.build();
    }


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

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
//        http
//                .authorizeHttpRequests(req -> req
//                        .requestMatchers("/", "/login", "/admin/login", "/join", "/admin/register", "/admin/verify", "/home").permitAll()
//                        .requestMatchers("/admin/mypage/**").hasRole("ADMIN")
//                        .requestMatchers("/admin/**").hasRole("ADMIN")
//                        .anyRequest().hasRole("MEMBER")
//                );
//
//        http // form 로그인 설정
//                .formLogin(form -> form
//                        .loginPage("/login")
//                        .defaultSuccessUrl("/home", true)
//                        .failureUrl("/login?error=true") // 로그인 실패 시 이동할 페이지
//                        .permitAll()
//                )
//                .formLogin(form -> form
//                        .loginPage("/admin/login")
//                        .defaultSuccessUrl("/home", true)
//                        .permitAll()
//                )
//                .logout(logout -> logout
//                        .logoutUrl("/logout")
//                        .logoutSuccessUrl("/login?logout")
//                        .permitAll()
//                )
//                .csrf(csrf -> csrf
//                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
//                .sessionManagement(session -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
//                        .invalidSessionUrl("/login")
//                        .maximumSessions(1)
//                        .maxSessionsPreventsLogin(false)
//                )
//                .rememberMe(rememberMe -> rememberMe
//                        .userDetailsService(this.userDetailsService)
//                        .key("uniqueAndSecret")
//                        .tokenValiditySeconds(86400) // 24 hours
//                )
//                .addFilter(new CustomAuthenticationFilter(authenticationManager));
//
//        return http.build();
//    }

}