package _2024.winter.semoton.domain.progress.dto.response;

import _2024.winter.semoton.domain.user.dto.RankDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class GetRankingsResponse {
    List<RankDto> rankings;

}
