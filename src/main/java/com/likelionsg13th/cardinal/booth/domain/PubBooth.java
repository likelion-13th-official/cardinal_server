package com.likelionsg13th.cardinal.booth.domain;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("주점")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class PubBooth extends Booth{

    @Column(nullable = false)
    private String notice; //공지사항

    @Column(nullable = true)
    private String instagramUrl;

    @Column(nullable = false)
    private String menuImageUrl;

    @Column(nullable = false)
    private String tableLayoutUrl; //주점 별 자리 배치도 그림

    @Column(nullable = false)
    private LocalDateTime noticeUpdatedAt; //공지사항 업데이트


    @PrePersist
    public void prePersist() {
        this.noticeUpdatedAt = LocalDateTime.now();
    }


}
