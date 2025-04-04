package _2024.winter.semoton.domain.progress.service;

import _2024.winter.semoton.common.apiPayload.failure.customException.LandmarkException;
import _2024.winter.semoton.common.apiPayload.failure.customException.UserException;
import _2024.winter.semoton.domain.landmark.repository.LandmarkRepository;
import _2024.winter.semoton.domain.progress.dto.response.QuestSubmitResponse;
import _2024.winter.semoton.domain.landmark.entity.Landmark;
import _2024.winter.semoton.domain.progress.entity.Grade;
import _2024.winter.semoton.domain.progress.entity.Progress;
import _2024.winter.semoton.domain.progress.repository.ProgressRepository;
import _2024.winter.semoton.domain.user.entity.User;
import _2024.winter.semoton.domain.user.jwt.JWTUtil;
import _2024.winter.semoton.domain.user.repository.UserRepository;
import _2024.winter.semoton.external.ai.AiService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ProgressCommandService {
    private final ProgressRepository progressRepository;
    private final UserRepository userRepository;
    private final LandmarkRepository landmarkRepository;
    private final JWTUtil jwtUtil;
    private final AiService aiService;

    public QuestSubmitResponse questSubmit(String landmarkName, String stdUrl, String clientUrl, HttpServletRequest httpServletRequest){
        log.info("[ProgressCommandService - questSubmit]");

        // 1. 유저 조회
        String username = jwtUtil.getUsername(httpServletRequest.getHeader("access"));
        User user = userRepository.findByUsername(username).orElseThrow(UserException.UsernameNotExistException::new);

        // 2. 랜드마크 조회
        Landmark landmark = landmarkRepository.findByName(landmarkName).orElseThrow(LandmarkException.LandmarkNotExistException::new);

        // 3. ai 서버로 사진 전송.
        int getScore = aiService.submitQuestImage(stdUrl, clientUrl);
        boolean cleared = getScore >= 10;

        if (!progressRepository.existsByUserAndLandmark(user, landmark)){
            // 4. progress 저장
            Progress progress = Progress.builder()
                    .user(user)
                    .landmark(landmark)
                    .cleared(cleared)
                    .score(getScore)
                    .grade(getGrade(getScore))
                    .build();

            progressRepository.save(progress);

            // 5. 유저 totalScore 업데이트
            user.updateTotalScore(getScore);

        }
        else{
            Progress progress = progressRepository.findByUserAndLandmark(user, landmark);
            int prevScore = progress.getScore();

            // 4. progress 업데이트(score, clear, grade)
            progress.updateProgress(getScore, cleared, getGrade(getScore));

            // 6. 유저 totalScore 업데이트
            user.updateTotalScore(-prevScore + getScore);
        }

        //  user allCleared 업데이트
        if (progressRepository.countByUserAndClearedTrue(user) == landmarkRepository.findAll().size())
            user.updateAllCleared();


        // 획득 점수 반환
        return QuestSubmitResponse.builder()
                .score(getScore)
                .build();
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
