package com.likelionsg13th.cardinal.common.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Embeddable
public class DetailImage {
    @Column(name = "imageUrl")
    private String imageUrl;
}
