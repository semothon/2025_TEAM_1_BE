package _2024.winter.semoton.common.apiPayload.failure;

import _2024.winter.semoton.common.apiPayload.failure.customException.LandmarkException;
import _2024.winter.semoton.common.apiPayload.failure.customException.UserException;
import _2024.winter.semoton.common.apiPayload.failure.customExceptionStatus.LandmarkExceptionStatus;
import _2024.winter.semoton.common.apiPayload.failure.customExceptionStatus.UserExceptionStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    // [USER]
     @ExceptionHandler(UserException.UserIdNotExistException.class)
        public ResponseEntity<ExceptionApiResponse> handleException(UserException.UserIdNotExistException e){
            log.error("[GlobalExceptionHandler] UserException.UserIdNotExistException occurred");
            return ResponseEntity
                    .status(
                            UserExceptionStatus.USERID_NOT_EXIST.getHttpStatus()
                    )
                    .body(
                            new ExceptionApiResponse(
                                    false, UserExceptionStatus.USERID_NOT_EXIST.getCode(), UserExceptionStatus.USERID_NOT_EXIST.getMessage()
                            )
                    );
        }

    @ExceptionHandler(UserException.UsernameNotExistException.class)
    public ResponseEntity<ExceptionApiResponse> handleException(UserException.UsernameNotExistException e){
        log.error("[GlobalExceptionHandler] UserException.UsernameNotExistException occurred");
        return ResponseEntity
                .status(
                        UserExceptionStatus.USERNAME_NOT_EXIST.getHttpStatus()
                )
                .body(
                        new ExceptionApiResponse(
                                false, UserExceptionStatus.USERNAME_NOT_EXIST.getCode(), UserExceptionStatus.USERNAME_NOT_EXIST.getMessage()
                        )
                );
    }
    @ExceptionHandler(UserException.UsernameAlreadyExistException.class)
    public ResponseEntity<ExceptionApiResponse> handleException(UserException.UsernameAlreadyExistException e){
        log.error("[GlobalExceptionHandler] UserException.UsernameAlreadyExistException occurred");
        return ResponseEntity
                .status(
                        UserExceptionStatus.USERNAME_ALREADY_EXIST.getHttpStatus()
                )
                .body(
                        new ExceptionApiResponse(
                                false, UserExceptionStatus.USERNAME_ALREADY_EXIST.getCode(), UserExceptionStatus.USERNAME_ALREADY_EXIST.getMessage()
                        )
                );
    }
    @ExceptionHandler(UserException.PasswordNotValidException.class)
    public ResponseEntity<ExceptionApiResponse> handleException(UserException.PasswordNotValidException e){
        log.error("[GlobalExceptionHandler] UserException.PasswordNotValidException occurred");
        return ResponseEntity
                .status(
                        UserExceptionStatus.PASSWORD_NOT_VALID.getHttpStatus()
                )
                .body(
                        new ExceptionApiResponse(
                                false, UserExceptionStatus.PASSWORD_NOT_VALID.getCode(), UserExceptionStatus.PASSWORD_NOT_VALID.getMessage()
                        )
                );
    }
    @ExceptionHandler(UserException.RefreshTokenNotValidException.class)
    public ResponseEntity<ExceptionApiResponse> handleException(UserException.RefreshTokenNotValidException e){
        log.error("[GlobalExceptionHandler] UserException.RefreshTokenNotValidException occurred");
        return ResponseEntity
                .status(
                        UserExceptionStatus.REFRESHTOKEN_NOT_VALID.getHttpStatus()
                )
                .body(
                        new ExceptionApiResponse(
                                false, UserExceptionStatus.REFRESHTOKEN_NOT_VALID.getCode(), UserExceptionStatus.REFRESHTOKEN_NOT_VALID.getMessage()
                        )
                );
    }

    // [LANDMARK]
    @ExceptionHandler(LandmarkException.LandmarkNotExistException.class)
    public ResponseEntity<ExceptionApiResponse> handleException(LandmarkException.LandmarkNotExistException e){
        log.error("[GlobalExceptionHandler] LandmarkException.LandmarkNotExistException occurred");
        return ResponseEntity
                .status(
                        LandmarkExceptionStatus.LANDMARK_NOT_EXIST.getHttpStatus()
                )
                .body(
                        new ExceptionApiResponse(
                                false, LandmarkExceptionStatus.LANDMARK_NOT_EXIST.getCode(), LandmarkExceptionStatus.LANDMARK_NOT_EXIST.getMessage()
                        )
                );
    }

}