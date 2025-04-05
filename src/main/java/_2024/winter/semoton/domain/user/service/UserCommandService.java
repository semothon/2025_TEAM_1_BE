package _2024.winter.semoton.domain.user.service;

import _2024.winter.semoton.common.apiPayload.failure.customException.UserException;
import _2024.winter.semoton.domain.user.dto.request.RegisterRequest;
import _2024.winter.semoton.domain.user.dto.response.LoginResponse;
import _2024.winter.semoton.domain.user.entity.User;
import _2024.winter.semoton.domain.user.jwt.JWTUtil;
import _2024.winter.semoton.domain.user.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserCommandService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;
    private final StringRedisTemplate stringRedisTemplate;

    public void register(RegisterRequest request){
        log.info("[UserCommandService - register]");

        // username 중복확인
        if(userRepository.existsByUsername(request.getUsername())){
            throw new UserException.UsernameAlreadyExistException();
        }

        // 비밀번호 encryption 후 save
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .nickname(request.getNickname())
                .totalScore(0)
                .allCleared(false)
                .role("ROLE_USER")
                .build();

        userRepository.save(user);
    }

    public LoginResponse issueToken(Long userId, HttpServletResponse httpServletResponse){
        log.info("[UserCommandService - issueToken]");
        User user = userRepository.findById(userId).get();

        String accessToken = jwtUtil.createJwt("access", user.getUsername(), user.getRole(), 10 * 60 * 1000L); // 10분
        String refreshToken = jwtUtil.createJwt("refresh", user.getUsername(), user.getRole(), 24 * 60 * 60 * 1000L); // 1일

        String redisRefreshKey = "refresh:userId:" + user.getId();
        stringRedisTemplate.opsForValue().set(redisRefreshKey, refreshToken, 1, TimeUnit.DAYS);

        httpServletResponse.setHeader("access", accessToken);
        httpServletResponse.setHeader("Set-Cookie", getResponseCookie(refreshToken));


        return LoginResponse.builder()
                .userId(user.getId())
                .nickname(user.getNickname())
                .build();
    }

    public void reissueToken(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        log.info("[UserCommandService - reissueToken]");
        // cookie refresh 추출
        String refreshToken = null;
        Cookie[] cookies = httpServletRequest.getCookies();

        for (Cookie cookie : cookies){
            if (cookie.getName().equals("refresh")){
                refreshToken = cookie.getValue();
            }
        }
        // redis refresh 추출
        User user = userRepository.findByUsername(jwtUtil.getUsername(refreshToken)).orElseThrow(UserException.PasswordNotValidException::new);
        String redisRefreshKey = "refresh:userId:" + user.getId();
        String redisRefreshValue = stringRedisTemplate.opsForValue().get(redisRefreshKey);

        // refresh 유효성 검증
        if (!jwtUtil.isValidRefreshToken(refreshToken, redisRefreshValue)){
            throw new UserException.RefreshTokenNotValidException();
        }

        // redis refresh 삭제
        stringRedisTemplate.delete(redisRefreshKey);

        // access, refresh 모두 재발급
        String newAccessToken = jwtUtil.createJwt("access", user.getUsername(), jwtUtil.getRole(refreshToken), 10 * 60 * 1000L);
        String newRefreshToken = jwtUtil.createJwt("refresh", user.getUsername(), jwtUtil.getRole(refreshToken), 24 * 60 * 60 * 1000L);

        // new refresh 1 day 유지
        stringRedisTemplate.opsForValue().set(redisRefreshKey, newRefreshToken, 1, TimeUnit.DAYS);

        httpServletResponse.setHeader("access", newAccessToken);
        httpServletResponse.setHeader("Set-Cookie", getResponseCookie(refreshToken));
    }

    private String getResponseCookie(String refreshToken){
        return ResponseCookie.from("refresh", refreshToken)
                .httpOnly(false)  // JavaScript에서 접근 불가
                .secure(false)    // HTTPS에서만 전송 아님
                .sameSite("None") // CORS 환경에서 허용
                .path("/")
                .maxAge(Duration.ofDays(1))
                .build().toString();
    }
}
