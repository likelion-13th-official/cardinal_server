package com.likelionsg13th.cardinal.booth.domain;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("PubBooth")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PubBooth extends Booth{

    private String notice; //공지사항
    private String instagramUrl;
    private String menuImageUrl;
    private String tableLayoutUrl; //주점 별 자리 배치도 그림
}
