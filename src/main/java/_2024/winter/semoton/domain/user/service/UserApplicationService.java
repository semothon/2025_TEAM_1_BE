package _2024.winter.semoton.domain.user.service;

import _2024.winter.semoton.domain.progress.dto.response.GetRankingsResponse;
import _2024.winter.semoton.domain.user.dto.request.LoginRequest;
import _2024.winter.semoton.domain.user.dto.request.RegisterRequest;
import _2024.winter.semoton.domain.user.dto.response.LoginResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserApplicationService {
    private final UserCommandService userCommandService;
     private final UserQueryService userQueryService;

    public void register(RegisterRequest request){
        userCommandService.register(request);
    }

    public LoginResponse login(LoginRequest request, HttpServletResponse httpServletResponse){
        Long userId = userQueryService.checkUserInfo(request);
        return userCommandService.issueToken(userId, httpServletResponse);
    }

    public void reissueToken(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        userCommandService.reissueToken(httpServletRequest, httpServletResponse);
    }

    public GetRankingsResponse getRankings(HttpServletRequest httpServletRequest){
        return userQueryService.getRankings(httpServletRequest);
    }
}
