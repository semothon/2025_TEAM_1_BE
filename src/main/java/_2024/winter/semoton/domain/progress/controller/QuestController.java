package _2024.winter.semoton.domain.progress.controller;

import _2024.winter.semoton.common.apiPayload.success.SuccessApiResponse;
import _2024.winter.semoton.domain.progress.dto.response.QuestSubmitResponse;
import _2024.winter.semoton.domain.progress.service.ProgressApplicationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class QuestController {

    private final ProgressApplicationService progressApplicationService;

    // 퀘스트 제출
    @PostMapping("/landmarks/{landmarkName}/quest")
    public SuccessApiResponse<QuestSubmitResponse> questSubmit(
            @PathVariable(name = "landmarkName") String landmarkName,
            @RequestParam(name = "stdImage") MultipartFile stdImage,
            @RequestParam(name = "clientImage") MultipartFile clientImage,
            HttpServletRequest httpServletRequest)
    {
        log.info("[QuestController - questSubmit]");

        // 요청의 Content-Type 출력
        System.out.println("Content-Type: " + httpServletRequest.getContentType());

        if (stdImage == null || clientImage == null) {
            throw new RuntimeException("파일이 제대로 전달되지 않았습니다.");
        }

        return SuccessApiResponse.QuestSubmit(progressApplicationService.questSubmit(landmarkName, stdImage, clientImage, httpServletRequest));
    }

}
