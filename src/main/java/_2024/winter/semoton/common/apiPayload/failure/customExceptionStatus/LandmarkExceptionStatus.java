package _2024.winter.semoton.common.apiPayload.failure.customExceptionStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum LandmarkExceptionStatus {

    LANDMARK_NOT_EXIST(HttpStatus.BAD_REQUEST, "40100", "존재하지 않는 랜드마크입니다."),

    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
