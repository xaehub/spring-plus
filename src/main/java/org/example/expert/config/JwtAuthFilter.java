package org.example.expert.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.expert.domain.user.enums.UserRole;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {

        String tokenValue = request.getHeader("Authorization");
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith("Bearer ")) {
            String token = jwtUtil.substringToken(tokenValue);

            try {
                // 유효성 검사 및 정보 추출
                Long userId = Long.parseLong(jwtUtil.extractClaims(token).getSubject());
                String email = jwtUtil.extractClaims(token).get("email", String.class);
                String roleStr = jwtUtil.extractClaims(token).get("userRole", String.class);
                String nickname = jwtUtil.extractClaims(token).get("nickname", String.class);
                UserRole userRole = UserRole.of(roleStr);

                // request에 정보 세팅
                request.setAttribute("userId", userId);
                request.setAttribute("email", email);
                request.setAttribute("nickname", nickname);
                request.setAttribute("userRole", roleStr);

                // Spring Security용 인증 정보 설정
                UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userId, null, userRole.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                log.error("JWT 처리 중 오류 발생: {}", e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }
}
