package com.likelionsg13th.cardinal.auth.service;

import com.likelionsg13th.cardinal.auth.domain.RefreshToken;
import com.likelionsg13th.cardinal.auth.dto.TokenResponse;
import com.likelionsg13th.cardinal.auth.jwt.JwtTokenProvider;
import com.likelionsg13th.cardinal.auth.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshRepo;

    public TokenResponse issueToken(String subject) {
        String access = jwtTokenProvider.createAccessToken(subject);
        String  refresh = jwtTokenProvider.createRefreshToken(subject);

        refreshRepo.deleteBySubject(subject);
        refreshRepo.save(RefreshToken.builder()
                .token(refresh)
                .subject(subject)
                .build());

        return TokenResponse.of(access, refresh);
    }

    public TokenResponse refresh(String refreshToken) {
        if(!jwtTokenProvider.isRefreshToken(refreshToken)) {
            throw new IllegalArgumentException("Not a refresh token");
        }

        String subject = jwtTokenProvider.getSubject(refreshToken);

        refreshRepo.findByToken(refreshToken)
                .orElseThrow(()-> new IllegalArgumentException("Refresh token not found"));
        return issueToken(subject);
    }


    /** 로그아웃(현재 세션): 해당 refresh 삭제 → 즉시 무효화 */
    public void logoutByRefresh(String refreshToken) {
        refreshRepo.deleteByToken(refreshToken);
    }

    /** 모든 세션 로그아웃: subject 기준 전체 refresh 삭제 */
    public void logoutAllSessions(String subject) {
        refreshRepo.deleteBySubject(subject);
    }


}
