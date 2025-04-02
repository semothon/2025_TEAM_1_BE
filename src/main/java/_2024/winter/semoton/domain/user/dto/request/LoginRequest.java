package _2024.winter.semoton.domain.user.dto.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
