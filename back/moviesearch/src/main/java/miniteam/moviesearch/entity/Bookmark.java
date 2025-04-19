package miniteam.moviesearch.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity // 이 클래스는 DB 테이블과 연결됨
@Data
@Table(name = "Bookmarks", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "movieId"})
}) // 테이블 이름을 bookmarks로 지정하고, user_id + movieId 조합이 중복되지 않도록 유니크 제약 추가
public class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    // 기본 키 (id) 자동 증가

    // 사용자
    @ManyToOne  // 북마크는 여러 개지만 사용자(User)는 하나 즉 N:1관계
    @JoinColumn(name = "user_id")
    private User user;

    // 북마크한 영화의 TMDb ID, 제목, 포스터 URL
    private String movieId;
    private String title;
    private String posterUrl;
}
