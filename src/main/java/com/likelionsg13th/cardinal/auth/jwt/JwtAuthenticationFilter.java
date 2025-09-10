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

/*    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String p = request.getRequestURI();
        // CORS preflight도 통과
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) return true;

        // 토큰 없이도 접근/처리해야 하는 엔드포인트는 필터 제외
        return p.startsWith("/auth/refresh")
                || p.startsWith("/auth/logout")
                || p.startsWith("/oauth2/")
                || p.equals("/login");
    }*/

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // 모든 요청에서 여기 로그가 찍혀야 정상 (OPTIONS 포함)
        String rawUri = request.getRequestURI();        // 예: /cardinal/auth/refresh 또는 /api/auth/refresh
        String ctx = request.getContextPath();          // 예: /cardinal (없을 수도)
        String servlet = request.getServletPath();      // 보통 스프링 서블릿 매핑 경로
        String method = request.getMethod();

        // 컨텍스트패스 제거한 "앱 내부 경로"
        String uri = rawUri;
        if (ctx != null && !ctx.isEmpty() && uri.startsWith(ctx)) {
            uri = uri.substring(ctx.length());
        }

        boolean skip = uri.startsWith("/auth/refresh")
                || uri.startsWith("/auth/token/exchange")
                || uri.startsWith("/auth/logout")
                || uri.startsWith("/oauth2/")
                || uri.equals("/login")
                || uri.startsWith("/pubOffice") //지우면 큰일납니다!
                || uri.startsWith("/health")
                || uri.startsWith("/css/") || uri.startsWith("/js/")
                || uri.startsWith("/images/") || uri.startsWith("/webjars/")
                || uri.equals("/favicon.ico");

        System.out.println(String.format(
                "[JWT-SNF] method=%s rawUri=%s ctx=%s servlet=%s normalizedUri=%s skip=%s",
                method, rawUri, ctx, servlet, uri, skip
        ));
        return skip;
    }

/*    @Override
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
    }*/




    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {

        String header = req.getHeader(HttpHeaders.AUTHORIZATION);


        System.out.println("[JWT] uri=" + req.getRequestURI());

        // 토큰이 아예 없으면 익명으로 통과 (여기서 401 만들지 말 것)
        if (header == null || !header.startsWith("Bearer ")) {
            org.springframework.security.core.context.SecurityContextHolder.clearContext();
            chain.doFilter(req, res);
            return;
        }

        String token = header.substring(7);
        try {
            var jws = jwt.parse(token);
            String subject = jws.getBody().getSubject();

            var principal = org.springframework.security.core.userdetails.User
                    .withUsername(subject)
                    .password("")
                    .authorities(List.of())
                    .build();

            var auth = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);



            chain.doFilter(req, res);



        } catch (ExpiredJwtException e) {
            // 만료 토큰: 여기서 바로 401로 끝내려면 'return' 필수
           writeUnauthorized(res, "TOKEN_EXPIRED",  "Access token has expired");
            return; // ✅ 중요: 체인 중단
            //throw new org.springframework.security.authentication.InsufficientAuthenticationException("expired", e);

        } catch (JwtException | IllegalArgumentException e) {
            writeUnauthorized(res, "INVALID_TOKEN", "Invalid or malformed token");
            return; // ✅ 중요: 체인 중단
            //throw new org.springframework.security.authentication.BadCredentialsException("invalid", e);
        }
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

