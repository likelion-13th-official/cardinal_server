package com.likelionsg13th.cardinal.pubOffice.dto.response;


import com.likelionsg13th.cardinal.auth.dto.TokenResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class PubAdminLoginResposne {
    Long pubId;
    String accessToken;
    String refreshToken;
    String tokenType;

    public static PubAdminLoginResposne  of(Long pubId,TokenResponse tokenResponse){
        return PubAdminLoginResposne.builder()
                .pubId(pubId)
                .accessToken(tokenResponse.getAccessToken())
                .refreshToken(tokenResponse.getRefreshToken())
                .tokenType(tokenResponse.getTokenType())
                .build();
    }
}
