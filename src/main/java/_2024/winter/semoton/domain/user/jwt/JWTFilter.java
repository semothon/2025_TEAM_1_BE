package _2024.winter.semoton.domain.user.jwt;

import _2024.winter.semoton.domain.user.dto.userDetails.RoleUserDetails;
import _2024.winter.semoton.domain.user.dto.userDetails.UserAccessDto;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // h2 db 연결은 필터 제외
        String requestURI = request.getRequestURI();
        if (requestURI.startsWith("/h2")
                || requestURI.startsWith("/favicon")
                || requestURI.startsWith("/api/health")
                || requestURI.startsWith("/api/register")
                || requestURI.startsWith("/api/login")
                || requestURI.startsWith("/api/reissue")) {
            filterChain.doFilter(request, response);
            return;
        }

        String access = request.getHeader("access");

        // validation1 - token null
        if (access == null) {
            System.out.println("token null");
            filterChain.doFilter(request, response);
            return;
        }

        // validation2 - token expire
        try {
            jwtUtil.isExpired(access);
        } catch (ExpiredJwtException e) {
            PrintWriter writer = response.getWriter();
            writer.print("access token expired");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // validation3 - token isn't access
        String category = jwtUtil.getCategory(access);

        if (!category.equals("access")) {
            PrintWriter writer = response.getWriter();
            writer.print("invalid access token");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String username = jwtUtil.getUsername(access);
        String role = jwtUtil.getRole(access);

        UserAccessDto userAccessDto = UserAccessDto.builder()
                .username(username)
                .role(role)
                .build();

        // USER
        if (role.equals("ROLE_USER")){
            RoleUserDetails roleUserDetails = new RoleUserDetails(userAccessDto);

            Authentication authentication = new UsernamePasswordAuthenticationToken(roleUserDetails, null, roleUserDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        }
        // ADMIN
        else{
            filterChain.doFilter(request, response);
        }



    }
}
