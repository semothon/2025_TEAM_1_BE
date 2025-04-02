package _2024.winter.semoton.domain.user.controller;

import _2024.winter.semoton.common.apiPayload.success.SuccessApiResponse;
import _2024.winter.semoton.domain.progress.dto.response.GetRankingsResponse;
import _2024.winter.semoton.domain.user.service.UserApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class RankController {
    private final UserApplicationService userApplicationService;

    @GetMapping("/rankings")
    public SuccessApiResponse<GetRankingsResponse> getRankings()
    {
        log.info("[RankController - getRankings]");

        return SuccessApiResponse.GetRankings(userApplicationService.getRankings());
    }
}
