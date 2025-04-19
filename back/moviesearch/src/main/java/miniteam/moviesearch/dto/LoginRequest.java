package miniteam.moviesearch.dto;

import lombok.Data;

@Data       // Lombok이 getter/setter 자동 생성
public class LoginRequest {
    private String username;
    private String password;
}   // 클라이언트가 JSON으로 보낸 로그인 정보를 자동으로 받아오는 객체
