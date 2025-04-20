package miniteam.moviesearch.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component // 이 필터를 Spring Bean으로 등록
@RequiredArgsConstructor // final 필드 자동 주입
public class JwtAuthenticationFilter extends OncePerRequestFilter { // 요청마다 한 번만 실행되는 필터 (Spring Security 전용)
    private final JwtTokenProvider jwtTokenProvider; // 토큰을 해석하고 사용자 정보를 꺼낼 유틸 클래스

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                     HttpServletResponse response,
                                     FilterChain filterChain)
            throws ServletException, IOException { // 필터가 실행될 때 호출되는 핵심 메서드
        String authHeader = request.getHeader("Authorization"); // 요청 헤더 중 Authorization을 꺼냄 예: Bearer eyJhdGciOi..."

        if (authHeader != null && authHeader.startsWith("Bearer ")) { // 헤더가 null이 아니고 "Bearer "로 시작할 경우에만 처리함
            String token = authHeader.substring(7); // "Bearer " 제거하고 실제 JWT만 남김
            String username = jwtTokenProvider.getUsername(token); // JWT에서 sub필드에 있는 username 꺼내기

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, null);
            // Spring Security가 이해할 수 있는 "인증 객체" 생성
            // 첫 번째 인자는 username, 권한은 지금은 null로 비워둠
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            // 추가로 요청 정보(request)를 설정 (IP, 세션 등)
            SecurityContextHolder.getContext().setAuthentication(authentication);
            // 이 요청은 "username"이라는 인증된 사용자가 보낸 요청이라고 등록함
        }
        filterChain.doFilter(request, response); // 다음 필터로 요청을 넘김
        // 인증이 없으면 넘어가고, 있으면 인증된 상태로 다음 컨트롤러로 감
    }
}
