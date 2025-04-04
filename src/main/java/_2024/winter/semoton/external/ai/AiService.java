package _2024.winter.semoton.external.ai;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AiService {
    private final WebClient webClient;

    public int submitQuestImage(String stdUrl, String clientUrl) {
        // JSON 요청 바디 생성
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("stdUrl", stdUrl);
        requestBody.put("clientUrl", clientUrl);

        try {
            // POST 요청 및 응답 처리
            Map<String, Object> response = webClient.post()
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .onErrorResume(WebClientResponseException.class, ex -> {
                        // 응답 에러 처리
                        throw new RuntimeException("AI 서버 응답 오류: " + ex.getMessage());
                    })
                    .block(); // 동기식 호출

            if (response == null || !response.containsKey("score")) {
                throw new RuntimeException("AI 서버 응답에 score가 없습니다.");
            }

            return (int) response.get("score");

        } catch (Exception e) {
            // 네트워크 오류 또는 기타 예외
            throw new RuntimeException("AI 서버 호출 실패: " + e.getMessage());
        }
    }
}
