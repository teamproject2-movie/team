package miniteam.moviesearch.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import miniteam.moviesearch.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest request) {
        authService.signup(request.getUsername(), request.getPassword());
        return ResponseEntity.ok("회원가입 성공");
    }

    @Data
    public static class SignupRequest {
        private String username;
        private String password;
    }
}
