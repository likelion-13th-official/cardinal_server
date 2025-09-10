package com.likelionsg13th.cardinal.auth.jwt;

import com.likelionsg13th.cardinal.pubOffice.service.CustomUserDetails;
import com.likelionsg13th.cardinal.pubOffice.service.PubAdminDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationAdminFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final PubAdminDetailService pubAdminDetailService;

    /*CustomUserDetail*/
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = resolveToken(request);
        // 토큰이 유효할 경우
        if (token != null && jwtTokenProvider.validate(token)) {
            // 토큰에서 사용자 ID (subject)
            String adminId = jwtTokenProvider.getSubject(token);

            // UserDetailsService를 통해 CustomUserDetails 객체를 불러옵니다.
            CustomUserDetails userDetails = pubAdminDetailService.loadUserByUsername(adminId);

            // 인증 객체를 만들 때, principal 자리에 userDetails 객체 전체를 넣습니다.
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, //  String adminId가 아닌, CustomUserDetails 객체
                    "",
                    userDetails.getAuthorities()
            );

            // SecurityContext에 인증 정보 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}
