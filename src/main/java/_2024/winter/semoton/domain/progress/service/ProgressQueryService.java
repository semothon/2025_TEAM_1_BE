package _2024.winter.semoton.domain.progress.service;

import _2024.winter.semoton.common.apiPayload.failure.customException.UserException;
import _2024.winter.semoton.domain.landmark.dto.brief.LandmarkBriefDto;
import _2024.winter.semoton.domain.landmark.entity.Landmark;
import _2024.winter.semoton.domain.landmark.repository.LandmarkRepository;
import _2024.winter.semoton.domain.progress.dto.brief.ProgressBriefDto;
import _2024.winter.semoton.domain.progress.dto.response.GetPresignedUrlResponse;
import _2024.winter.semoton.domain.progress.dto.response.GetProgressInfoResponse;
import _2024.winter.semoton.domain.progress.entity.Progress;
import _2024.winter.semoton.domain.progress.repository.ProgressRepository;
import _2024.winter.semoton.domain.user.entity.User;
import _2024.winter.semoton.domain.user.jwt.JWTUtil;
import _2024.winter.semoton.domain.user.repository.UserRepository;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ProgressQueryService {
    private final ProgressRepository progressRepository;
    private final LandmarkRepository landmarkRepository;
    private final UserRepository userRepository;
    private final AmazonS3 amazonS3;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private static final double EARTH_RADIUS = 6378000; // 지구 반지름(m)




    public GetProgressInfoResponse getProgressInfo(Long userId, double latitude, double longitude){
        log.info("[ProgressQueryService - getProgressInfo]");

        User user = userRepository.findById(userId).orElseThrow(UserException.UserIdNotExistException::new);

        List<ProgressBriefDto> progressList = progressRepository.findAllByUserOrderByLandmarkId(user)
                .stream()
                .map(ProgressBriefDto::new)
                .toList();

        List<LandmarkBriefDto> landmarkList = new ArrayList<>();
        for(Landmark landmark : landmarkRepository.findAll()){
            landmarkList.add(
                    new LandmarkBriefDto(
                            landmark.getName(),
                            isWithinDistance(latitude, longitude, landmark.getLatitude(), landmark.getLongitude(), 50)
                    )
            );
        }

        return GetProgressInfoResponse.builder()
                .progressList(progressList)
                .landmarkList(landmarkList)
                .build();

    }

    public static boolean isWithinDistance(double lat1, double lon1, double lat2, double lon2, double threshold) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = EARTH_RADIUS * c; // 두 지점 간 거리 (미터 단위)

        return distance <= threshold;
    }

    public GetPresignedUrlResponse getPresignedUrl(String imageName) {
        /// 제한시간 설정
        Date expiration = new Date();
        long expTime = expiration.getTime();
        expTime += TimeUnit.MINUTES.toMillis(3); // 3 Minute
        expiration.setTime(expTime);

        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucket, imageName)
                .withMethod(HttpMethod.PUT)
                .withExpiration(expiration);

        GetPresignedUrlResponse response = GetPresignedUrlResponse.builder()
                .presignedUrl(amazonS3.generatePresignedUrl(generatePresignedUrlRequest).toString())
                .build();
        return response;

    }
}
