package _2024.winter.semoton.domain.user.service;

import _2024.winter.semoton.common.apiPayload.failure.customException.UserException;
import _2024.winter.semoton.domain.progress.dto.response.GetRankingsResponse;
import _2024.winter.semoton.domain.progress.entity.Grade;
import _2024.winter.semoton.domain.user.dto.RankDto;
import _2024.winter.semoton.domain.user.dto.request.LoginRequest;
import _2024.winter.semoton.domain.user.entity.User;
import _2024.winter.semoton.domain.user.jwt.JWTUtil;
import _2024.winter.semoton.domain.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserQueryService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;

    public Long checkUserInfo(LoginRequest loginRequest){
        log.info("[UserQueryService - login]");

        // username 확인
        User user = userRepository.findByUsername(loginRequest.getUsername()).orElseThrow(UserException.UsernameNotExistException::new);

        // password 확인
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())){
            throw new UserException.PasswordNotValidException();
        }

        return user.getId();
    }

    public GetRankingsResponse getRankings(HttpServletRequest httpServletRequest){
        log.info("[UserQueryService - getRankings]");

        String username = jwtUtil.getUsername(httpServletRequest.getHeader("access"));
        User my = userRepository.findByUsername(username).orElseThrow(UserException.UsernameNotExistException::new);

        List<RankDto> rankings = new ArrayList<>();

        List<User> allClearedUserList = userRepository.findByAllClearedTrue();
        allClearedUserList.sort(Comparator.comparingInt(User::getTotalScore).reversed());

        int curScore = userRepository.findMaxTotalScore();
        int curOrder = 1;

        for(User user: allClearedUserList){
            if (user.getTotalScore() < curScore){
                curOrder++;
                curScore = user.getTotalScore();
            }
            rankings.add(new RankDto(curOrder, markingUsername(user.getUsername()), user.getNickname(), user.getTotalScore(), getGrade((int)(user.getTotalScore()/3))));
        }

        return GetRankingsResponse.builder()
                .myNickName(my.getNickname())
                .myTotalScore(my.getTotalScore())
                .rankings(rankings.subList(0, Math.min(10, allClearedUserList.size())))
                .build();
    }

    private String markingUsername(String username){
        StringBuilder sb = new StringBuilder();
        for(int i = 0 ; i < username.length(); i++){
            if (i < username.length() - 4) sb.append("*");
            else sb.append(username.charAt(i));
        }
        return sb.toString();
    }

    private Grade getGrade(int score){
        return switch (score / 10) {
            case 10 -> Grade.A_PLUS;
            case 9 -> Grade.A;
            case 8 -> Grade.A_MINUS;
            case 7 -> Grade.B_PLUS;
            case 6 -> Grade.B;
            case 5 -> Grade.B_MINUS;
            case 4 -> Grade.C_PLUS;
            case 3 -> Grade.C;
            case 1 -> Grade.C_MINUS;
            default -> Grade.F;
        };
    }
}
