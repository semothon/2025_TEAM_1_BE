package _2024.winter.semoton.domain.user.dto;

import _2024.winter.semoton.domain.progress.entity.Grade;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RankDto {
    int order;
    String username;
    String nickname;
    int totalScore;
    Grade grade;

    @Builder
    public RankDto(int order, String username, String nickname, int totalScore, Grade grade){
        this.order = order;
        this.username = username;
        this.nickname = nickname;
        this.totalScore = totalScore;
        this.grade = grade;
    }

}
