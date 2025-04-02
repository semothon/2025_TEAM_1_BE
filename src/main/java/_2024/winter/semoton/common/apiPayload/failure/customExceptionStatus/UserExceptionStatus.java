package _2024.winter.semoton.common.apiPayload.failure.customExceptionStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserExceptionStatus {
    USERID_NOT_EXIST(HttpStatus.BAD_REQUEST, "40000", "존재하지 않는 사용자입니다."),
    USERNAME_NOT_EXIST(HttpStatus.BAD_REQUEST, "40001", "존재하지 않는 사용자 아이디입니다."),
    USERNAME_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "40002", "이미 존재하는 사용자 아이디입니다."),
    PASSWORD_NOT_VALID(HttpStatus.BAD_REQUEST, "40003", "비밀번호가 유효하지 않습니다."),
    REFRESHTOKEN_NOT_VALID(HttpStatus.BAD_REQUEST, "40004", "토큰이 유효하지 않습니다."),

    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}

