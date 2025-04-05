package _2024.winter.semoton.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginResponse {
    private Long userId;
    private String nickname;

    @Builder
    public LoginResponse(Long userId, String nickname) {
        this.userId = userId;
        this.nickname = nickname;
    }
}
