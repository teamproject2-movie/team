package miniteam.moviesearch.JwtTokenProvider;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider { //Spring Bean으로 등록되는 JWT 도구 클래스
    private Key key;    // key는 JWT 서명을 위한 비밀 키

    @PostConstruct
    public void init() {
        this.key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }       // 앱 실행 시 한 번 생성 (임시 방식, 실서비스에선 환경변수나 .env로 분리)

    public String generateToken(String username) {
        long now = System.currentTimeMillis();
        long expireInMs = 1000 * 60 * 60; // 현제 시간 기준으로 1시간짜리 토큰 생성

        return Jwts.builder()   // JWT를 생성해서 문자열로 반환
                .setSubject(username)   //username을 토큰안에 넣음
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + expireInMs))
                .signWith(key)
                .compact();
    }

    public String getUsername(String token) {   // 이 메서드는 나중에 토큰을 파싱해서사용자 이름을 꺼내는 용도 예: 북마크할 때 로그인된 사용자를 알아내기 위해 사용
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody().getSubject();
    }
}
