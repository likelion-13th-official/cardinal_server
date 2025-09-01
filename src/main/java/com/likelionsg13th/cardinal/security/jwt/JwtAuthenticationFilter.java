package com.likelionsg13th.cardinal.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.List;



@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwt;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {
        String header = req.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                //유효성 만료시
                var jws = jwt.parse(token);
                // String subject = jwt.getSubject(token);
                String subject = jws.getBody().getSubject(); // "kakao:{id}"
                var principal = org.springframework.security.core.userdetails.User
                        .withUsername(subject)
                        .password("")                  // 비번/권한 미사용
                        .authorities(List.of())
                        .build();
/*                SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities())
                );*/
                var auth = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);


            } catch (ExpiredJwtException e) {
                // 만료: 401 + WWW-Authenticate 헤더 + JSON 바디
                writeUnauthorized(res, "TOKEN_EXPIRED", "Access token has expired");
                return;
            } catch (JwtException | IllegalArgumentException e) {
                // 위변조/포맷 오류 등: 401
                writeUnauthorized(res, "INVALID_TOKEN", "Invalid or malformed token");
                return;
            }
        }
        chain.doFilter(req, res);
    }


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        // OAuth2 로그인 경로는 필터 적용 제외
        String path = request.getRequestURI();
        return path.startsWith("/oauth2/") || path.startsWith("/auth/") /*|| path.startsWith("/booths")*/ || path.startsWith("/events") || path.startsWith("/goods") || path.startsWith("/search");
    }

    private void writeUnauthorized(HttpServletResponse res, String code, String message) throws IOException {
        res.setStatus(HttpStatus.UNAUTHORIZED.value());
        // RFC 6750 권장: WWW-Authenticate에 error, error_description
        res.setHeader("WWW-Authenticate", "Bearer error=\"invalid_token\", error_description=\"" + message + "\"");
        res.setContentType("application/json;charset=UTF-8");
        res.getWriter().write("""
        {"success":false,"code":"%s","message":"%s"}
        """.formatted(code, message));

    }
}

