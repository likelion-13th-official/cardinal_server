package com.likelionsg13th.cardinal.goods.domain;

import com.likelionsg13th.cardinal.common.domain.DetailImage;
import com.likelionsg13th.cardinal.map.domain.Map;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
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

    @Column(nullable = false, columnDefinition = "TEXT")
    private String thumbnailUrl;

    @Column(nullable = false)
    private Long price;

    @Column(nullable = false)
    private Long viewCount=0L;

    @CollectionTable(name="goods_detail_images",joinColumns = @JoinColumn(name="goods_id"))
    @ElementCollection
    @OrderColumn(name = "image_order")
    private List<DetailImage> detailImageList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Map location;



}

