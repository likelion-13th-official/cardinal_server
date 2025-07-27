package com.likelionsg13th.cardinal.common.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Embeddable
public class DetailImage {

    private String imageUrl;

    //추후 유효성 검사 추가 로직 구현 ,
}
