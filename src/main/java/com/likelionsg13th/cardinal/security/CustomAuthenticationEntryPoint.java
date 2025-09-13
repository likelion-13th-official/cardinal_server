package com.likelionsg13th.cardinal.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelionsg13th.cardinal.auth.jwt.JwtAuthenticationFilter;
import com.likelionsg13th.cardinal.common.dto.resonseDto.ApiResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper mapper = new ObjectMapper();
/*
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

      log.warn("EntryPoint 401: uri={}, exType={}, msg={}", request.getRequestURI(), authException.getClass().getSimpleName(), authException.getMessage());
        System.out.println("[ENTRY] method=" + request.getMethod()
                + " uri=" + request.getRequestURI()
                + " ctx=" + request.getContextPath()
                + " servlet=" + request.getServletPath()
                + " query=" + request.getQueryString());

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        ApiResponse apiResponse = new ApiResponse(false, 401, "인증이 필요한 서비스입니다.", null);

        // ObjectMapper를 사용하여 ApiResponse 객체를 JSON 문자열로 변환 후 응답 스트림에 작성
        response.getWriter().write(mapper.writeValueAsString(apiResponse));}*/


        @Override
        public void commence(HttpServletRequest request,
                HttpServletResponse response,
                AuthenticationException ex) throws IOException {

            if (response.isCommitted()) return;

            int code = HttpServletResponse.SC_UNAUTHORIZED; // 401
            String message = "인증이 필요한 서비스입니다.";

            if (ex instanceof JwtAuthenticationFilter.JwtAuthException jae) {
                switch (jae.getReason()) {
                    case EXPIRED -> message = "토큰이 만료되었습니다";
                    case INVALID -> message = "잘못된 토큰입니다";
                    default -> message = "인증 토큰 처리 중 오류가 발생했습니다";
                }
            }

            log.warn("EntryPoint 401: uri={}, exType={}, msg={}",
                    request.getRequestURI(), ex.getClass().getSimpleName(), ex.getMessage());

            response.setStatus(code);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");

            // Map.of는 null 불가 → LinkedHashMap 사용
            Map<String, Object> body = new LinkedHashMap<>();
            body.put("success", false);
            body.put("code", code);
            body.put("message", message == null ? "" : message);
            body.put("data", null);

            response.getWriter().write(mapper.writeValueAsString(body));
        }

}
