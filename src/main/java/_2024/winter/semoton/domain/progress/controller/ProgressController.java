package _2024.winter.semoton.domain.progress.controller;

import _2024.winter.semoton.common.apiPayload.success.SuccessApiResponse;
import _2024.winter.semoton.domain.progress.dto.response.GetProgressInfoResponse;
import _2024.winter.semoton.domain.progress.service.ProgressApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProgressController {
    private final ProgressApplicationService progressApplicationService;

    // 진행 상황 조회
    // 유저가 참가한 랜드마크 + 거리기반 참가 가능한 랜드마크
    @GetMapping("/progress/users/{userId}")
    public SuccessApiResponse<GetProgressInfoResponse> getProgressInfo(
            @PathVariable(name = "userId") Long userId,
            @RequestParam(name = "latitude") double latitude,
            @RequestParam(name = "longitude") double longitude)
    {
        log.info("[ProgressController - getProgressInfo]");
        return SuccessApiResponse.GetProgressInfo(progressApplicationService.getProgressInfo(userId, latitude, longitude));
    }
}
