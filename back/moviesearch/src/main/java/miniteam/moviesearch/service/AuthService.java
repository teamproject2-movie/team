package miniteam.moviesearch.service;

import lombok.RequiredArgsConstructor;
import miniteam.moviesearch.entity.User;
import miniteam.moviesearch.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service        // 이 클래스는 비즈니스 로작 담당 (Spring Bean)
@RequiredArgsConstructor        // final 필드 자동 생성자 주입
public class AuthService {

    private final UserRepository userRepository;    // DB에 사용자 저장/조회
    private final PasswordEncoder passwordEncoder;  // 비밀번호를 암호화할 때 사용


    public void signup(String username, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("이미 존재하는 사용자입니다.");
        }       // 사용자가 이미 DB에 존재하면 예외 발생
        User user = new User(); // 새로운 User 객체 생성
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password)); // 비밀번호는 평문이 아닌 BCrypt 암호화 후 저장
        userRepository.save(user); // DB에 저장
    }

    public String login(String username, String password) { // DB에서 username으로 사용자 조회
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자가 존재하지 않습니다.")); // 없으면 예외 발생

        if (!passwordEncoder.matches(password, user.getPassword())) { // 평문 비밀번호를 암호화된 비밀번호와 비교
            throw new RuntimeException("비밀번호가 틀렸습니다.");     // 일치하지 않으면 예외 발생

        }

        return jwtTokenProvider.generateToken(user.getUsername()); // 검증을 통과하면 username을 넣은 JWT 토큰 생성 후 반환
    }
}
