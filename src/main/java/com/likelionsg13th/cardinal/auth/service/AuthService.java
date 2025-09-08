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
    private final OneTimeCodeService codeService;

    /** ★ 리팩토링 핵심: 원타임 코드 교환을 서비스로 이동 */
    public TokenResponse exchangeOneTimeCode(String code) {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("code_required");
        }
        String subject = codeService.consume(code); // 1회성 소비
        if (subject == null) {
            throw new IllegalArgumentException("invalid_or_expired_code");
        }
        return issueToken(subject);
    }

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

    public TokenResponse issueToken(String adminId,Long pudId) {
        String access = jwtTokenProvider.createAccessToken(adminId);
        String  refresh = jwtTokenProvider.createPubAdminAccessToken(adminId,pudId);

        refreshRepo.deleteBySubject(adminId);
        refreshRepo.save(RefreshToken.builder()
                .token(refresh)
                .subject(adminId)
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
/*
        //일반적으로 logout은 refresh 토큰이 있든 없든 무조건 삭제 및 ok 반환하는  idempotent 사용
        // refresh 토큰없는 경우, 400에러 반환하는 strict 로그아웃은 잘 쓰지 않음.
        var opt = refreshRepo.findByToken(refreshToken);
        if(opt.isEmpty()) {
            if(strict){
                throw new IllegalArgumentException("Refresh token not found");
            }else{
                return;
            }
        }*/
        refreshRepo.deleteByToken(refreshToken);
    }

    /** 모든 세션 로그아웃: subject 기준 전체 refresh 삭제 */
    public void logoutAllSessions(String subject) {
        refreshRepo.deleteBySubject(subject);
    }


}
