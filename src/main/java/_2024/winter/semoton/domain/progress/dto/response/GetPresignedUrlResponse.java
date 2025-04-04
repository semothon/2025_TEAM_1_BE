package _2024.winter.semoton.domain.progress.dto.response;

import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetPresignedUrlResponse {
    public String presignedUrl;
}
