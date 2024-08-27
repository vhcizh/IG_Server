package iGuard.Server.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http // 보안 경로 지정
                .authorizeHttpRequests(req -> req
                        .requestMatchers("/login").permitAll() // 일반 유저 로그인 페이지
                        .requestMatchers("/admin/login").permitAll() // 관계자 로그인 페이지
                        .requestMatchers("/join").permitAll() // 일반 유저 회원가입
                        .requestMatchers("/admin/join").permitAll() // 관계자 회원가입
                        .anyRequest().authenticated() // 나머지 요청은 인증 필요
                );

        http // form 로그인 설정
                .formLogin(form -> form
                        .loginPage("/login") // 일반 유저 로그인 페이지
                        .usernameParameter("id") // 일반 유저 로그인 시 사용할 파라미터
                        .passwordParameter("password")
                        .defaultSuccessUrl("/") // 로그인 성공 후 이동할 페이지
                        .failureUrl("/login?error=true") // 로그인 실패 시 이동할 페이지
                        .permitAll()
                );
//                .formLogin(form -> form
//                        .loginPage("/admin/login") // 관계자 로그인 페이지
//                        .usernameParameter("adminId") // 관계자 로그인 시 사용할 파라미터
//                        .passwordParameter("adminPassword")
//                        .defaultSuccessUrl("/admin/home") // 로그인 성공 후 이동할 페이지
//                        .failureUrl("/admin/login?error=true") // 로그인 실패 시 이동할 페이지
//                        .permitAll()
//                );
        http.logout(Customizer.withDefaults());
        /**
         * TODO: CSRF 설정 활성화
         * 현재 임시로 비활성화해뒀지만 CSRF 설정 필요
         */
        http.csrf(AbstractHttpConfigurer::disable);
        // iframe 허용
        http.headers(headers -> headers
                .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));


        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
