package _2024.winter.semoton.domain.progress.entity;

import _2024.winter.semoton.domain.landmark.entity.Landmark;
import _2024.winter.semoton.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "progresses")
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Progress {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // 사용자

    @ManyToOne
    @JoinColumn(name = "landmark_id")
    private Landmark landmark; // 해당 맵

    @Column(nullable = false)
    private boolean cleared; // 맵 클리어 여부

    @Column(nullable = false)
    private int score; // 퀘스트 점수

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Grade grade;

    @Builder
    public Progress(User user, Landmark landmark, boolean cleared, int score, Grade grade) {
        this.user = user;
        this.landmark = landmark;
        this.cleared = cleared;
        this.score = score;
        this.grade = grade;
    }

    public void updateProgress(int score, boolean cleared, Grade grade){
        if (this.score < score){
            this.score = score;
            this.grade = grade;
            if (!this.cleared) this.cleared = cleared;
        }
    }
}
