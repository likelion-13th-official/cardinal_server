package com.likelionsg13th.cardinal.auth.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Table(name = "refresh_token", indexes = {
        @Index(name = "idx_refresh_subject", columnList = "subject")
})
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 512)
    private String token;

    @Column(nullable = false, length = 191)
    private String subject; // "kakao:{id}"

    public void updateToken(String newToken) {
        this.token = newToken;
    }

}
