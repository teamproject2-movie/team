package miniteam.moviesearch.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity  // Soring Security 설정을 적용하겠다는 뜻
@RequiredArgsConstructor
public class SecurityConfig { // 시큐리티 설정 클래스

    private final JwtAuthenticationFilter jwtAuthenticationFilter; // 우리가 만든 필터를 주입 받아서 보안 필터 체인에 등록할 예정

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Spring Security의 보안 설정을 커스터마이징 하는 메서드

        return http
                .csrf(csrf -> csrf.disable()) // CSRF(사이트간 요청 위조) 방지 기능은 지금은 꺼둠 (JWT 기반 앱에서는 주로 비활성화)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/api/auth/**",
                                "/api/movies/Search",
                                "/login/**",
                                "/register/**",
                                "/MovieFinder/**",
                                "/logo.png"
                        ).permitAll()   // 회원가입, 로그인은 인증없이 가능
                        .anyRequest().authenticated()       // 그 외의 행동은 인증된 사용자만 접근 가능
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                // UsernamePasswordAuthenticationFiter보다 먼저 우리가 만든 JWT 필터를 실행하도록 설정
                .build(); // 최종 설정 객체 반환
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    } // 비밀번호 암호화에 사용할 인코더 등록

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    } // 인증 매니저는 로그인할 때 사용할 수 있도록 Beam 등록
}
