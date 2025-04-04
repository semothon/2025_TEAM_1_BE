package _2024.winter.semoton.domain.progress.service;

import _2024.winter.semoton.domain.progress.dto.response.GetProgressInfoResponse;
import _2024.winter.semoton.domain.progress.dto.response.QuestSubmitResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProgressApplicationService {
    private final ProgressCommandService progressCommandService;
    private final ProgressQueryService progressQueryService;

    public QuestSubmitResponse questSubmit(String landmarkName, String stdUrl, String clientUrl, HttpServletRequest httpServletRequest){
        return progressCommandService.questSubmit(landmarkName, stdUrl, clientUrl, httpServletRequest);
    }

    public GetProgressInfoResponse getProgressInfo(Long userId, double latitude, double longitude){
        return progressQueryService.getProgressInfo(userId, latitude, longitude);
    }



}
