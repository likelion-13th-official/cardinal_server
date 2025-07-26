package com.likelionsg13th.cardinal.goods.domain;

import com.likelionsg13th.cardinal.common.domain.DetailImage;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Goods {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private String thumbnailUrl;
//    @Column(nullable = false)
//    private List<DetailImage> detailImageList;
}
