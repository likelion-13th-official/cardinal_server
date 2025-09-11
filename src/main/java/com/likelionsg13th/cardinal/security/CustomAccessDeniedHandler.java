package com.likelionsg13th.cardinal.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelionsg13th.cardinal.common.dto.resonseDto.ApiResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper mapper = new ObjectMapper();
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        System.out.println("[ENTRY] method=" + request.getMethod()
                + " uri=" + request.getRequestURI()
                + " ctx=" + request.getContextPath()
                + " servlet=" + request.getServletPath()
                + " query=" + request.getQueryString());

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        ApiResponse apiResponse = new ApiResponse(false, 403, "해당 작업을 수행할 권한이 없습니다.", null);

        // ObjectMapper를 사용하여 ApiResponse 객체를 JSON 문자열로 변환 후 응답 스트림에 작성
        response.getWriter().write(mapper.writeValueAsString(apiResponse));

    }
}
