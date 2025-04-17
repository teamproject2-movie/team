package miniteam.moviesearch.entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity     // 이 클래스는 JPA 엔티티 (DB 테이블과 매핑)
@Data       // getter/setter/toString 등 자동 생성
@Table(name = "users")  // DB 테이블 이름을 users로 지정
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // 기본키 (id)는 자동 증가 방식으로 설정

    @Column(unique = true)
    private String username;    // username은 중복 불가(unique)

    private String password;    //password는 암호화된 비밀번호가 저장됨
}
