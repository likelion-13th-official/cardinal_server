package com.likelionsg13th.cardinal.auth.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
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
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String p = request.getRequestURI();
        // CORS preflight도 통과
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) return true;

        // 토큰 없이도 접근/처리해야 하는 엔드포인트는 필터 제외
        return p.startsWith("/auth/refresh")
                || p.startsWith("/auth/logout")
                || p.startsWith("/oauth2/")
                || p.equals("/login");
    }

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
            } catch (JwtException | IllegalArgumentException e) {
                // 위변조/포맷 오류 등: 401
                writeUnauthorized(res, "INVALID_TOKEN", "Invalid or malformed token");
            }
        }
        chain.doFilter(req, res);
        return;
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

