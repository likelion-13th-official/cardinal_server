package com.likelionsg13th.cardinal.users.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter @Setter
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nickname;

    @Column
    private String profileImageUrl;

    @Column(length = 30, nullable = false)
    private String provider;    // "kakao"

    @Column(length = 100, nullable = false, unique = true)
    private String providerId;  // 카카오 id
}
