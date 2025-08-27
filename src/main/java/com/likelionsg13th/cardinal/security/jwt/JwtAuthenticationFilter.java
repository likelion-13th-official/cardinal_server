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
                SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities())
                );
            } catch (Exception ignored) { }
        }
        chain.doFilter(req, res);
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