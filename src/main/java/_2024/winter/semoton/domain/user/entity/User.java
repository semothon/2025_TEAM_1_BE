package _2024.winter.semoton.domain.user.entity;

import _2024.winter.semoton.domain.progress.entity.Progress;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Entity
@Table(name = "users")
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    private Long id;

    @Column(nullable = false, unique = true)
    private String username; // 사용자 ID

    @Column(nullable = false)
    private String password; // 비밀번호

    @Column(nullable = false)
    private String nickname; // 닉네임

    @Column(nullable = false)
    private String role; // 닉네임

    @OneToMany(mappedBy = "user")
    private List<Progress> progressList; // 사용자의 진행 상황

    @Column(name = "totalScore")
    private int totalScore;

    @Column(name = "allCleared")
    private boolean allCleared;

    @Builder
    public User(String username, String password, String nickname, String role, int totalScore, boolean allCleared) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.role = role;
        this.totalScore = totalScore;
        this.allCleared = allCleared;
    }


    public void updateTotalScore(int score){
        this.totalScore += score;
    }

    public void updateAllCleared(){
        this.allCleared = true;
    }
}