package miniteam.moviesearch.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import miniteam.moviesearch.dto.*;
import miniteam.moviesearch.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController     // 이 컨트롤러 가 REST API라는것을 의미
@RequestMapping("/api/auth")        // 모든 경로 앞에 /api/auth 가 자동으로 붙
@RequiredArgsConstructor               // final 필드인 authService를 생성자 주입으로 자동 처리
public class AuthController {

    private final AuthService authService;  // AuthService를 의존성 주입 받음
    // 회원가입 로직은 이 서비스에서 처리함

    @PostMapping("/signup") // POST /api/auth/signup 요청을 처리
    public ResponseEntity<?> signup(@RequestBody SignupRequest request) {   // 클라이언트에서 보낸 JSON을 SignupRequest 객체로 자동 매핑
        authService.signup(request.getUsername(), request.getPassword(), request.getEmail());   // 사용자 저장 비즈니스 호출
        return ResponseEntity.ok("회원가입 성공");        // 응답 HTTP 200 OK로 "회원가입 성공" 반환
    }

    @PostMapping("/login")      // 로그인 요청 처리
    public ResponseEntity<?> login(@RequestBody LoginRequest request) { //@RequestBody: JSON 요창 본문을 LoginRequest 객체로 매핑
        String token = authService.login(request.getUsername(), request.getPassword()); // 사용자 검증 -> JWT 발급
        return ResponseEntity.ok().body("Bearer " + token); // 응답은 "Bearer {토큰}" 형식 (나중에 HTTP 헤더에 담아서 인증할 때 사용)
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody UserUpdateRequest request) {
        authService.updateUser(request.getPassword(), request.getEmail());
        return ResponseEntity.ok("사용자 정보가 수정되었습니다.");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteuser() {
        authService.deleteUser();
        return ResponseEntity.ok("사용자 계정이 삭제되었습니다.");
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@RequestBody UserProfileRequest request) {
        authService.updateProfileImage(request.getProfileImage());
        return ResponseEntity.ok("프로필 이미지가 변경되었습니다.");
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getProfile() {
        return ResponseEntity.ok(authService.getProfileImage());
    }
}
