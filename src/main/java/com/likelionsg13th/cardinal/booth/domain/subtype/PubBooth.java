package com.likelionsg13th.cardinal.booth.domain.subtype;


import com.likelionsg13th.cardinal.booth.domain.Booth;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("주점")
@Getter
@SuperBuilder @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PubBooth extends Booth {

    private String notice; //공지사항

    private String instagramUrl;

    private String tableLayoutUrl; //주점 별 자리 배치도 그림

    private LocalDateTime noticeUpdatedAt; //공지사항 업데이트


    @PrePersist
    public void prePersist() {
        this.noticeUpdatedAt = LocalDateTime.now();
    }

    public void updateNoticeAndDescription(String notice,String description) {
        this.notice = notice;
        super.updateDescription(description);
        this.noticeUpdatedAt = LocalDateTime.now();
    }


}
